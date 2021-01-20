package crypto.stackrs.stackrsservice.coinmarketcap;

import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import crypto.stackrs.stackrsservice.web.WebClientConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

@Data
@Service
@Slf4j
public class LatestListings {

  private final CoinmarketcapConfig coinmarketcapConfig;
  private final WebClientConfiguration webClientConfiguration;

  private static final String API_KEY = "X-CMC_PRO_API_KEY";
  private static final String START_PARAM = "start";
  private static final String LIMIT_PARAM = "limit";
  private static final String CONVERT_PARAM = "convert";

  public LatestListings(final CoinmarketcapConfig coinmarketcapConfig, final WebClientConfiguration webClientConfiguration) {
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.webClientConfiguration = webClientConfiguration;
  }

  public Listing listings() {
    return webClientConfiguration
      .webClientWithTimeout(coinmarketcapConfig.getBase_uri(), coinmarketcapConfig.getTimeout())
      .get()
      .uri(uriBuilder -> uriBuilder.path(coinmarketcapConfig.getLatest_listings_uri())
        .queryParam(START_PARAM, coinmarketcapConfig.getStart())
        .queryParam(LIMIT_PARAM, coinmarketcapConfig.getLimit())
        .queryParam(CONVERT_PARAM, coinmarketcapConfig.getConvert())
        .build())
      .header(API_KEY, coinmarketcapConfig.getCoinmarketcap_api_key())
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(Listing.class)
      .block();
  }
}
