package crypto.stackrs.stackrsservice.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BinanceAPI {

  private final SecurityConfig securityConfig;
  private final BinanceApiRestClient restClient;
  private final BinanceApiWebSocketClient webSocketClient;

  @Autowired
  public BinanceAPI(
    SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(this.securityConfig.getBinance_api_key(), this.securityConfig.getBinance_api_secret());
    this.restClient = factory.newRestClient();
    this.webSocketClient = factory.newWebSocketClient();
  }

  public BinanceApiRestClient getRestClient() {
    return restClient;
  }
  public BinanceApiWebSocketClient getWebSocketClient() { return webSocketClient; }

}
