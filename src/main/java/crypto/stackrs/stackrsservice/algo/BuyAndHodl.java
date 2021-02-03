package crypto.stackrs.stackrsservice.algo;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.exception.BinanceApiException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

@Slf4j
@Builder
public class BuyAndHodl implements Algo {

  private String pair;
  private double amount;
  private int precision;
  private BinanceApiRestClient binanceApiRestClient;

  @Override
  public void execute() {

    try {
      String precision = "%." + this.precision + "f";
      String amount = String.format (precision, this.amount);

      log.info("About to buy " + amount + " " + this.pair);

      this.binanceApiRestClient.newOrder(marketBuy(this.pair, "").quoteOrderQty(amount).newOrderRespType(NewOrderResponseType.RESULT));

    } catch( BinanceApiException ex) {
      log.warn(ex.getMessage());
    } catch( Exception ex) {
      log.error(ex.getMessage());
    }
  }
}
