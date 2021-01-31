package crypto.stackrs.stackrsservice;

import crypto.stackrs.stackrsservice.algo.Algo;
import crypto.stackrs.stackrsservice.algo.BuyAndHodl;
import crypto.stackrs.stackrsservice.algo.BuyLowSellHigh;
import crypto.stackrs.stackrsservice.coinmarketcap.CoinmarketcapQueries;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
class StackrsServiceApplicationTests {

  private final CoinmarketcapQueries coinmarketcapQueries;
  private final CoinmarketcapConfig coinmarketcapConfig;
  private final AlgoConfig algoConfig;
  private final Algo algo;

  @Autowired
  public StackrsServiceApplicationTests(
    @Qualifier("coinmarketcapqueriesimpl") CoinmarketcapQueries coinmarketcapQueries,
    @Qualifier("buyandhodlalgo") BuyAndHodl buyAndHodl,
    CoinmarketcapConfig coinmarketcapConfig, AlgoConfig algoConfig) {
    this.algo = buyAndHodl;
    this.coinmarketcapQueries = coinmarketcapQueries;
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.algoConfig = algoConfig;
  }

  @Test
  void contextLoads() {
  }

  @Test
  void canGetCoinmarketcapAPIListings() {

    Listing listings = coinmarketcapQueries.listings();

    assertThat(listings.getStatus().getError_code()).isEqualTo(0);
    assertThat(listings.getData().size()).isEqualTo(coinmarketcapConfig.getLimit());
    assertThat(listings.getData().get(0).getQuote().containsKey(algoConfig.getTarget_coin())).isTrue();

  }

  @Test
  void canExecuteBuyLowSellHigh() {
    //algo.execute();
  }

  @Test
  void canExecuteBuyAndHodl() {
    algo.execute();
  }

}
