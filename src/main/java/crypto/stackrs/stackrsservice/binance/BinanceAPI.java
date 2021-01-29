package crypto.stackrs.stackrsservice.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BinanceAPI {

  private final SecurityConfig securityConfig;
  private final BinanceApiRestClient restClient;

  @Autowired
  public BinanceAPI(
    SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(this.securityConfig.getBinance_api_key(), this.securityConfig.getBinance_api_secret());
    this.restClient = factory.newRestClient();
  }

  public BinanceApiRestClient getRestClient() {
    return restClient;
  }

}
