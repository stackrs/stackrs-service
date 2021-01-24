package crypto.stackrs.stackrsservice.binance;

import com.google.common.collect.HashMultimap;
import crypto.stackrs.stackrsservice.binance.accountsnapshot.AccountSnapshot;
import crypto.stackrs.stackrsservice.binance.accountsnapshot.SnapVos;
import crypto.stackrs.stackrsservice.binance.exchangeinfo.ExchangeInfo;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import crypto.stackrs.stackrsservice.config.BinanceConfig;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import crypto.stackrs.stackrsservice.config.WebClientConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Service
@Slf4j
@Qualifier("binancequeriesimpl")
public class BinanceQueriesImpl implements BinanceQueries {

  private final SecurityConfig securityConfig;
  private final BinanceConfig binanceConfig;
  private final AlgoConfig algoConfig;
  private final WebClientConfiguration webClientConfiguration;

  private static final String API_KEY = "X-MBX-APIKEY";
  private static final String TYPE_PARAM = "type";
  private static final String TIMESTAMP_PARAM = "timestamp";
  private static final String SIGNATURE_PARAM = "signature";

  public BinanceQueriesImpl(final SecurityConfig securityConfig,
                            final BinanceConfig binanceConfig,
                            final AlgoConfig algoConfig,
                            final WebClientConfiguration webClientConfiguration) {
    this.securityConfig = securityConfig;
    this.binanceConfig = binanceConfig;
    this.algoConfig = algoConfig;
    this.webClientConfiguration = webClientConfiguration;
  }

  @Override
  public ExchangeInfo exchangeinfo() {
    return webClientConfiguration
      .webClientWithTimeout(binanceConfig.getBase_uri(), binanceConfig.getTimeout())
      .get()
      .uri(uriBuilder -> uriBuilder.path(binanceConfig.getExchange_info_url())
        .build())
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(ExchangeInfo.class)
      .block();
  }

  @Override
  public AccountSnapshot accountsnapshot() {
    return webClientConfiguration
      .webClientWithTimeout(binanceConfig.getBase_uri(), binanceConfig.getTimeout())
      .get()
      .uri(uriBuilder -> uriBuilder.path(binanceConfig.getAccount_snapshot_uri())
        .queryParams(validatedQueryParams(
          new LinkedMultiValueMap<String, String>(
            Map.of(TYPE_PARAM, Arrays.asList(binanceConfig.getSnap_type())))))
        .build())
      .header(API_KEY, securityConfig.getBinance_api_key())
      .retrieve()
      .onStatus(HttpStatus::is4xxClientError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .onStatus(HttpStatus::is5xxServerError,
        error -> Mono.error(new RuntimeException("Server replied with error " + error.rawStatusCode())))
      .bodyToMono(AccountSnapshot.class)
      .block();
  }

  @Override
  public double targetCoinBalance() {
    return Double.parseDouble(accountsnapshot().getSnapshotVos().stream()
      .max(Comparator.comparing(SnapVos::getUpdateTime))
      .orElseThrow(NoSuchElementException::new)
      .getData()
      .getBalances()
      .stream()
      .filter(balance -> algoConfig.getTarget_coin().equals(balance.getAsset()))
      .findFirst()
      .orElseThrow(NoSuchElementException::new)
      .getFree());
  }

  private MultiValueMap<String, String> validatedQueryParams(MultiValueMap<String, String> query_params_in) {
    query_params_in.add(TIMESTAMP_PARAM, String.valueOf(System.currentTimeMillis()));
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    UriBuilder uri_builder = factory.builder().queryParams(query_params_in);
    query_params_in.add(SIGNATURE_PARAM, encrypted_key(uri_builder.build().getQuery()));
    return query_params_in;
  }

  private String encrypted_key(String message) {

    String encrypted = "default";

    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(securityConfig.getBinance_api_secret().getBytes(), "HmacSHA256");
      sha256_HMAC.init(secretKeySpec);
      encrypted = new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes())));
    } catch (NoSuchAlgorithmException e) {
      log.warn(e.getMessage());
    } catch (InvalidKeyException e) {
      log.warn(e.getMessage());
    }

    return encrypted;
  }
}
