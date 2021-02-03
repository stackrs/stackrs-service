package crypto.stackrs.stackrsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="algo")
@Data
public class AlgoConfig {
  private String stacking_pair;
  private double base_amount;
  private String target_crypto;
  private double target_amount;
  private double target_profit;
  private int default_precision;
}
