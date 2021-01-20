package crypto.stackrs.stackrsservice.coinmarketcap.listing;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Listing {
  private List<ListingEntry> data = new ArrayList<>();
  private Status status;
}
