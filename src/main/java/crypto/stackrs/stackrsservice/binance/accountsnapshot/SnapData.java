package crypto.stackrs.stackrsservice.binance.accountsnapshot;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SnapData {
  private String totalAssetOfBtc;
  private List<SnapBalance> balances = new ArrayList<>();
}
