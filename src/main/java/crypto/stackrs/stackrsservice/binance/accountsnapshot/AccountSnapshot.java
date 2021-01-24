package crypto.stackrs.stackrsservice.binance.accountsnapshot;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountSnapshot {
  int code;
  String msg;
  List<SnapVos> snapshotVos = new ArrayList<>();
}
