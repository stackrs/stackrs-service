package crypto.stackrs.stackrsservice.coinmarketcap.listing;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ListingEntry {
  private int id;
  private int cmc_rank;
  private String name;
  private String symbol;
  private Map<String, Quote> quote = new HashMap<>();
}
