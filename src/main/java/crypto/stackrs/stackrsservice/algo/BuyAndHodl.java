package crypto.stackrs.stackrsservice.algo;

import crypto.stackrs.stackrsservice.binance.BinanceAPI;
import crypto.stackrs.stackrsservice.config.AlgoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Qualifier("buyandhodlalgo")
public class BuyAndHodl implements Algo {

  private final AlgoConfig algoConfig;
  private final BinanceAPI binanceAPI;

  @Autowired
  public BuyAndHodl(
    AlgoConfig algoConfig, BinanceAPI binanceAPI) {
    this.algoConfig = algoConfig;
    this.binanceAPI = binanceAPI;
  }

  @Override
  public void execute() {

    double balance = Double.parseDouble(binanceAPI.getRestClient().getAccount().getAssetBalance(algoConfig.getBase_fiat()).getFree());

    if( balance < algoConfig.getTarget_fiat_amount()) {
      log.warn("Available " + algoConfig.getBase_fiat() + " balance " + balance + " is less than min balance " + algoConfig.getTarget_fiat_amount());
      return;
    }


  }
}
