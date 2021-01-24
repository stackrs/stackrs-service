package crypto.stackrs.stackrsservice.binance;

import crypto.stackrs.stackrsservice.binance.exchangeinfo.ExchangeInfo;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.BinanceConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import crypto.stackrs.stackrsservice.config.WebClientConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Data
@Service
@Slf4j
@Qualifier("binancequeriesimpl")
public class BinanceQueriesImpl implements BinanceQueries {

  private final SecurityConfig securityConfig;
  private final BinanceConfig binanceConfig;
  private final WebClientConfiguration webClientConfiguration;

  public BinanceQueriesImpl(final SecurityConfig securityConfig,
                            final BinanceConfig binanceConfig,
                            final WebClientConfiguration webClientConfiguration) {
    this.securityConfig = securityConfig;
    this.binanceConfig = binanceConfig;
    this.webClientConfiguration = webClientConfiguration;
  }

  @Override
  public ExchangeInfo exchangeinfo() {
    return webClientConfiguration
      .webClientWithTimeout(binanceConfig.getBase_uri(), binanceConfig.getTimeout())
      .get()
      .uri(uriBuilder -> uriBuilder.path(binanceConfig.getExchange_info_url())
        .build())
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(ExchangeInfo.class)
      .block();
  }
}
