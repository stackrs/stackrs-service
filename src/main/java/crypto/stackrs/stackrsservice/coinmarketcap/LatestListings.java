package crypto.stackrs.stackrsservice.coinmarketcap;

import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Data
@Service
@Slf4j
public class LatestListings {

  private final CoinmarketcapConfig coinmarketcapConfig;
  private final RestTemplate restTemplate;

  public LatestListings(final CoinmarketcapConfig coinmarketcapConfig, final RestTemplateBuilder restTemplateBuilder) {
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.restTemplate = restTemplateBuilder.build();
  }

  public void listings() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("X-CMC_PRO_API_KEY", coinmarketcapConfig.getCoinmarketcap_api_key());
    HttpEntity request = new HttpEntity(headers);

    try {
      ResponseEntity<String> test = this.restTemplate.exchange(coinmarketcapConfig.getLatest_listings_endpoint(), HttpMethod.GET, request, String.class);
      log.info(test.getBody());
    } catch ( HttpStatusCodeException ex) {
      log.error(ex.getStatusCode().toString());
      log.error(ex.getResponseBodyAsString());
    }
  }

}
