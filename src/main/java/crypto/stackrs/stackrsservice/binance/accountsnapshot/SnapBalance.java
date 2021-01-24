package crypto.stackrs.stackrsservice.binance.accountsnapshot;

import lombok.Data;

@Data
public class SnapBalance {
   private String asset;
   private String free;
   private String locked;
}
