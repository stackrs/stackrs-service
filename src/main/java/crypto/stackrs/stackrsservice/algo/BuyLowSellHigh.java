package crypto.stackrs.stackrsservice.algo;

import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.general.SymbolStatus;
import com.binance.api.client.exception.BinanceApiException;
import crypto.stackrs.stackrsservice.binance.BinanceAPI;
import crypto.stackrs.stackrsservice.coinmarketcap.CoinmarketcapQueries;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.Listing;
import crypto.stackrs.stackrsservice.coinmarketcap.listing.ListingEntry;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.limitSell;

@Data
@Service
@Slf4j
@Qualifier("buylowsellhighalgo")
public class BuyLowSellHigh implements Algo {

  private final AlgoConfig algoConfig;
  private final BinanceAPI binanceAPI;

  @Autowired
  public BuyLowSellHigh(
    AlgoConfig algoConfig, BinanceAPI binanceAPI) {
    this.algoConfig = algoConfig;
    this.binanceAPI = binanceAPI;
  }

  @Override
  public void execute() {

    double balance = Double.parseDouble(binanceAPI.getRestClient().getAccount().getAssetBalance(algoConfig.getTarget_crypto()).getFree());

    //if( balance < algoConfig.getTarget_amount()) {
    //  log.warn("Available " + algoConfig.getTarget_crypto() + " balance " + balance + " is less than min balance " + algoConfig.getTarget_amount());
    //  return;
   // }

   // Listing listing = coinmarketcapQueries.listings();

   // ListingEntry picked_symbol = listing.getData()
    //  .stream()
    //  .sorted(Comparator.comparing(ListingEntry::getTarget_coin_quote))
    //  .filter(coin -> canTrade(coin, algoConfig.getTarget_crypto()))
    //  .findFirst()
    //  .get();

    //String pair = picked_symbol.getSymbol().concat(algoConfig.getTarget_crypto());

   // double market_price = Double.parseDouble(binanceAPI.getRestClient().getPrice(pair).getPrice());
    //double limit_price = market_price + (market_price * algoConfig.getTarget_profit());
    //double amount = algoConfig.getTarget_amount() / market_price;

    //log.info("Executing: " + pair +
    //  ", %_change: " + picked_symbol.getTarget_coin_quote().getPercent_change_24h() +
    //  ", market_price: " + market_price +
    //  ", limit_price: " + limit_price +
    //  ", amount: " + amount);


    //NewOrderResponse newMarketOrderResponse = binanceAPI.getRestClient().newOrder(marketBuy(pair, String.valueOf(amount)).newOrderRespType(NewOrderResponseType.RESULT));
    //NewOrderResponse newLimitOrderResponse = binanceAPI.getRestClient().newOrder(limitSell(pair, TimeInForce.GTC, String.valueOf(amount), String.valueOf(limit_price)));

  }

  private boolean canTrade(ListingEntry coin, String target_coin) {
    boolean can_trade = true;

    String pair = coin.getSymbol().concat(algoConfig.getTarget_crypto());

    try {
      can_trade = binanceAPI.getRestClient().getExchangeInfo().getSymbolInfo(pair).getStatus() == SymbolStatus.TRADING;
    } catch(BinanceApiException ex) {
      log.info("Pair not tradeable on Binance: " + pair);
      can_trade = false;
    }

    return can_trade;
  }

}
