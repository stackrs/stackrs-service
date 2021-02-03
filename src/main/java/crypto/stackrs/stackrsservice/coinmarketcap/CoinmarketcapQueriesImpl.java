package crypto.stackrs.stackrsservice.coinmarketcap;

import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import crypto.stackrs.stackrsservice.config.WebClientConfiguration;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@Data
@Slf4j
@Builder
public class CoinmarketcapQueriesImpl implements CoinmarketcapQueries{

  private static final String API_KEY = "X-CMC_PRO_API_KEY";
  private static final String START_PARAM = "start";
  private static final String LIMIT_PARAM = "limit";
  private static final String CONVERT_PARAM = "convert";

  private WebClientConfiguration web_client_configuration;
  private String base_uri;
  private int timeout;
  private String latest_listings_uri;
  private int start;
  private int limit;
  private String target_crypto;
  private String coinmarketcap_api_key;

  @Override
  public Listing listings() {

    Listing listings = web_client_configuration
      .webClientWithTimeout(base_uri, timeout)
      .get()
      .uri(uriBuilder -> uriBuilder.path(latest_listings_uri)
        .queryParam(START_PARAM, start)
        .queryParam(LIMIT_PARAM, limit)
        .queryParam(CONVERT_PARAM, target_crypto)
        .build())
      .header(API_KEY, coinmarketcap_api_key)
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(Listing.class)
      .block();

    listings.getData().forEach((d) -> d.setTarget_coin_quote(d.getQuote().get(target_crypto)));

    return listings;
  }
}
