package crypto.stackrs.stackrsservice.binance;

import crypto.stackrs.stackrsservice.binance.accountsnapshot.AccountSnapshot;
import crypto.stackrs.stackrsservice.binance.exchangeinfo.ExchangeInfo;

public interface BinanceQueries {
  ExchangeInfo exchangeinfo();
  AccountSnapshot accountsnapshot();
}
