package crypto.stackrs.stackrsservice.coinmarketcap;

import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
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
@Qualifier("coinmarketcapqueriesimpl")
public class CoinmarketcapQueriesImpl implements CoinmarketcapQueries{

  private final AlgoConfig algoConfig;
  private final SecurityConfig securityConfig;
  private final CoinmarketcapConfig coinmarketcapConfig;
  private final WebClientConfiguration webClientConfiguration;

  private static final String API_KEY = "X-CMC_PRO_API_KEY";
  private static final String START_PARAM = "start";
  private static final String LIMIT_PARAM = "limit";
  private static final String CONVERT_PARAM = "convert";

  public CoinmarketcapQueriesImpl(final AlgoConfig algoConfig,
                                  final SecurityConfig securityConfig,
                                  final CoinmarketcapConfig coinmarketcapConfig,
                                  final WebClientConfiguration webClientConfiguration) {
    this.algoConfig = algoConfig;
    this.securityConfig = securityConfig;
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.webClientConfiguration = webClientConfiguration;
  }

  @Override
  public Listing listings() {
    Listing listings = webClientConfiguration
      .webClientWithTimeout(coinmarketcapConfig.getBase_uri(), coinmarketcapConfig.getTimeout())
      .get()
      .uri(uriBuilder -> uriBuilder.path(coinmarketcapConfig.getLatest_listings_uri())
        .queryParam(START_PARAM, coinmarketcapConfig.getStart())
        .queryParam(LIMIT_PARAM, coinmarketcapConfig.getLimit())
        .queryParam(CONVERT_PARAM, algoConfig.getTarget_coin())
        .build())
      .header(API_KEY, securityConfig.getCoinmarketcap_api_key())
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(Listing.class)
      .block();

    listings.getData().forEach((d) -> d.setTarget_coin_quote(d.getQuote().get(algoConfig.getTarget_coin())));

    return listings;
  }
}
