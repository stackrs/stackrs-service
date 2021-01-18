package crypto.stackrs.stackrsservice;

import crypto.stackrs.stackrsservice.coinmarketcap.LatestListings;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class StackrsServiceApplicationTests {

  @Autowired
  LatestListings latestListings;

  @Test
  void contextLoads() {
  }

  @Test
  void canGetCoinmarketcapAPIKey() {
    latestListings.listings();
  }
}
