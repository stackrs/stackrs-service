package crypto.stackrs.stackrsservice.binance.accountsnapshot;

import lombok.Data;

@Data
public class SnapVos {
  private String type;
  private long updateTime;
  private SnapData data;
}
