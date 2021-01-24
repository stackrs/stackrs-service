package crypto.stackrs.stackrsservice;

import crypto.stackrs.stackrsservice.binance.BinanceQueries;
import crypto.stackrs.stackrsservice.binance.accountsnapshot.AccountSnapshot;
import crypto.stackrs.stackrsservice.binance.exchangeinfo.ExchangeInfo;
import crypto.stackrs.stackrsservice.coinmarketcap.CoinmarketcapQueries;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.config.BinanceConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class StackrsServiceApplicationTests {

  private final CoinmarketcapQueries coinmarketcapQueries;
  private final CoinmarketcapConfig coinmarketcapConfig;
  private final BinanceQueries binanceQueries;
  private final BinanceConfig binanceConfig;

  @Autowired
  public StackrsServiceApplicationTests(
    @Qualifier("coinmarketcapqueriesimpl") CoinmarketcapQueries coinmarketcapQueries,
    @Qualifier("binancequeriesimpl") BinanceQueries binanceQueries,
    CoinmarketcapConfig coinmarketcapConfig, BinanceConfig binanceConfig) {
    this.coinmarketcapQueries = coinmarketcapQueries;
    this.binanceQueries = binanceQueries;
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.binanceConfig = binanceConfig;
  }

  @Test
  void contextLoads() {
  }

  @Test
  void canGetCoinmarketcapAPIListings() {

    Listing listings = coinmarketcapQueries.listings();

    assertThat(listings.getStatus().getError_code()).isEqualTo(0);
    assertThat(listings.getData().size()).isEqualTo(coinmarketcapConfig.getLimit());
    assertThat(listings.getData().get(0).getId()).isEqualTo(coinmarketcapConfig.getStart());
    assertThat(listings.getData().get(0).getQuote().containsKey(coinmarketcapConfig.getConvert())).isTrue();

  }

  @Test
  void canGetBinanceAPIExchangeInfo() {
    ExchangeInfo exchangeInfo = binanceQueries.exchangeinfo();

    assertThat(exchangeInfo.getSymbols().size()).isGreaterThan(0);
  }

  @Test
  void canGetBinanceAPIAccountSnapshot() {
    AccountSnapshot accountSnapshot = binanceQueries.accountsnapshot();

    assertThat(accountSnapshot.getSnapshotVos().size()).isGreaterThan(0);
  }

}
