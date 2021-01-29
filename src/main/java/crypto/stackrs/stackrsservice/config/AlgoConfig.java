package crypto.stackrs.stackrsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="algo")
@Data
public class AlgoConfig {
  private String target_coin;
  private double target_min_balance;
  private double target_profit;
}
