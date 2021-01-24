package crypto.stackrs.stackrsservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfiguration {

  public WebClient webClientWithTimeout(final String base_url, final int timeout) {
    final var tcpClient = TcpClient
      .create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
      .doOnConnected(connection -> {
        connection.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
        connection.addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
      });

    return WebClient.builder()
      .exchangeStrategies(ExchangeStrategies.builder()
        .codecs(configurer -> configurer
          .defaultCodecs()
          .maxInMemorySize(16 * 1024 * 1024))
        .build())
      .baseUrl(base_url)
      .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
      .filter(logRequest())
      .build();
  }

  private ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
      log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
      return Mono.just(clientRequest);
    });
  }
}
