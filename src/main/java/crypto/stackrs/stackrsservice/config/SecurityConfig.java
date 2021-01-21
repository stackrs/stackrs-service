package crypto.stackrs.stackrsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="security.config")
@Data
public class SecurityConfig {
  private String coinmarketcap_api_key;
}
