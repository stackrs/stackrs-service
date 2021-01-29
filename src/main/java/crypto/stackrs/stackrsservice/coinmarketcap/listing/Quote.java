package crypto.stackrs.stackrsservice.coinmarketcap.listing;

import lombok.Data;

@Data
public class Quote implements Comparable<Quote>{
  private double price;
  private double volume_24h;
  private double percent_change_1h;
  private Double percent_change_24h;
  private double percent_change_7d;
  private double market_cap;

  @Override
  public int compareTo(Quote au){
    return this.percent_change_24h.compareTo(au.percent_change_24h);
  }

}
