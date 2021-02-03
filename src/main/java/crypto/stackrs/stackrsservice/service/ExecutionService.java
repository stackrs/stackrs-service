package crypto.stackrs.stackrsservice.service;

import com.binance.api.client.domain.event.AccountUpdateEvent;
import com.binance.api.client.domain.event.BalanceUpdateEvent;
import com.binance.api.client.domain.event.OrderTradeUpdateEvent;
import com.binance.api.client.domain.event.UserDataUpdateEvent;
import crypto.stackrs.stackrsservice.algo.BuyAndHodl;
import crypto.stackrs.stackrsservice.binance.BinanceAPI;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import crypto.stackrs.stackrsservice.config.CoinmarketcapConfig;
import crypto.stackrs.stackrsservice.config.SecurityConfig;
import crypto.stackrs.stackrsservice.config.WebClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class ExecutionService {

  private final CoinmarketcapConfig coinmarketcapConfig;
  private final AlgoConfig algoConfig;
  private final SecurityConfig securityConfig;
  private final WebClientConfiguration webClientConfiguration;
  private final BinanceAPI binanceAPI;

  @Autowired
  public ExecutionService(
    CoinmarketcapConfig coinmarketcapConfig,
    AlgoConfig algoConfig,
    SecurityConfig securityConfig,
    WebClientConfiguration webClientConfiguration,
    BinanceAPI binanceAPI) {
    this.coinmarketcapConfig = coinmarketcapConfig;
    this.algoConfig = algoConfig;
    this.securityConfig = securityConfig;
    this.webClientConfiguration = webClientConfiguration;
    this.binanceAPI = binanceAPI;
  }

  @Scheduled(cron = "${algo.stacking_schedule}")
  void execute_buy_and_hodl() {
    BuyAndHodl.builder()
      .amount(algoConfig.getBase_amount())
      .pair(algoConfig.getStacking_pair())
      .precision(algoConfig.getDefault_precision())
      .binanceApiRestClient(binanceAPI.getRestClient())
      .build()
      .execute();
  }

  @PostConstruct
  public void start() {

    String key = binanceAPI.getRestClient().startUserDataStream();

    binanceAPI.getWebSocketClient().onUserDataUpdateEvent(key, response -> {
      if (response.getEventType() == UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE) {
        AccountUpdateEvent accountUpdateEvent = response.getAccountUpdateEvent();
        log.info("Account Update Event");
        log.info(accountUpdateEvent.toString());
      } else if (response.getEventType() == UserDataUpdateEvent.UserDataUpdateEventType.BALANCE_UPDATE){
        BalanceUpdateEvent balanceUpdateEvent = response.getBalanceUpdateEvent();
        log.info("Balance Update Event");
        log.info(balanceUpdateEvent.getEventType() + " for " + balanceUpdateEvent.getAsset());
      } else if (response.getEventType() == UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_POSITION_UPDATE) {
        log.info("Account Position Update Event");
        log.info(response.toString());
      } else if (response.getEventType() == UserDataUpdateEvent.UserDataUpdateEventType.ORDER_TRADE_UPDATE) {
        OrderTradeUpdateEvent orderTradeUpdateEvent = response.getOrderTradeUpdateEvent();
        log.info("Order Trade Update Event");
        log.info(orderTradeUpdateEvent.toString());
      }
    });

    binanceAPI.getRestClient().keepAliveUserDataStream(key);

  }
}
