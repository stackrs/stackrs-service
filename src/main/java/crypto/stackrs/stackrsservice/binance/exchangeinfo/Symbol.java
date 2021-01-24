package crypto.stackrs.stackrsservice.binance.exchangeinfo;

import lombok.Data;

@Data
public class Symbol {
  private String symbol;
  private String status;
  private String baseAsset;
  private int baseAssetPrecision;
  private String quoteAsset;
  private int quotePrecision;
  private int quoteAssetPrecision;
  private int baseCommissionPrecision;
  private int quoteCommissionPrecision;
  private boolean isSpotTradingAllowed;
}
