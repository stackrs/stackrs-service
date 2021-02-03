package crypto.stackrs.stackrsservice;

import crypto.stackrs.stackrsservice.algo.BuyAndHodl;
import crypto.stackrs.stackrsservice.binance.BinanceAPI;
import crypto.stackrs.stackrsservice.coinmarketcap.CoinmarketcapQueries;
import crypto.stackrs.stackrsservice.coinmarketcap.CoinmarketcapQueriesImpl;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import crypto.stackrs.stackrsservice.config.WebClientConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
class StackrsServiceApplicationTests {

  private final CoinmarketcapConfig coinmarketcapConfig;
  private final AlgoConfig algoConfig;
  private final SecurityConfig securityConfig;
  private final WebClientConfiguration webClientConfiguration;
  private final BinanceAPI binanceAPI;

  @Autowired
  public StackrsServiceApplicationTests(
    CoinmarketcapConfig coinmarketcapConfig,
    AlgoConfig algoConfig,
    SecurityConfig securityConfig,
    WebClientConfiguration webClientConfiguration,
    BinanceAPI binanceAPI) {
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.algoConfig = algoConfig;
    this.securityConfig = securityConfig;
    this.webClientConfiguration = webClientConfiguration;
    this.binanceAPI = binanceAPI;
  }

  @Test
  void contextLoads() {
  }

  @Test
  void canGetCoinmarketcapAPIListings() {

    /*Listing listings = CoinmarketcapQueriesImpl.builder()
      .coinmarketcap_api_key(securityConfig.getCoinmarketcap_api_key())
      .base_uri(coinmarketcapConfig.getBase_uri())
      .latest_listings_uri(coinmarketcapConfig.getLatest_listings_uri())
      .start(coinmarketcapConfig.getStart())
      .limit(coinmarketcapConfig.getLimit())
      .target_crypto(algoConfig.getTarget_crypto())
      .timeout(coinmarketcapConfig.getTimeout())
      .web_client_configuration(webClientConfiguration)
      .build()
      .listings();

    assertThat(listings.getStatus().getError_code()).isEqualTo(0);
    assertThat(listings.getData().size()).isEqualTo(coinmarketcapConfig.getLimit());
    assertThat(listings.getData().get(0).getQuote().containsKey(algoConfig.getTarget_crypto())).isTrue();
  */
  }

}
