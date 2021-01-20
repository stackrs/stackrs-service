package crypto.stackrs.stackrsservice.coinmarketcap.listing;

import lombok.Data;

@Data
public class Status {
  private int error_code;
  private int elapsed;
  private int credit_count;
  private String timestamp;
  private String error_message;
}
