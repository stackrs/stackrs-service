package crypto.stackrs.stackrsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="api.config.coinmarketcap")
@Data
public class CoinmarketcapConfig {
  private String base_uri;
  private String coinmarketcap_api_key;
  private String latest_listings_uri;
  private int timeout;
  private int start;
  private int limit;
  private String convert;
}
