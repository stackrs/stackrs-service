package crypto.stackrs.stackrsservice.service;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AccountUpdateEvent;
import com.binance.api.client.domain.event.BalanceUpdateEvent;
import com.binance.api.client.domain.event.OrderTradeUpdateEvent;
import com.binance.api.client.domain.event.UserDataUpdateEvent;
import crypto.stackrs.stackrsservice.binance.BinanceAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class UserStream {

  private final BinanceAPI binanceAPI;

  UserStream(BinanceAPI binanceAPI) {
    this.binanceAPI = binanceAPI;
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
