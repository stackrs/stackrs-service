package crypto.stackrs.stackrsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="api.config.binance")
@Data
public class BinanceConfig {
  private String base_uri;
  private String exchange_info_url;
  private String account_snapshot_uri;
  private int timeout;
  private String snap_type;
}
