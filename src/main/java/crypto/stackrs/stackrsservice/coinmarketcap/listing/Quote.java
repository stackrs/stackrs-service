package crypto.stackrs.stackrsservice.coinmarketcap.listing;

import lombok.Data;

@Data
public class Quote {
  private double price;
  private double volume_24h;
  private double percent_change_1h;
  private double percent_change_24h;
  private double percent_change_7d;
  private double market_cap;
}
