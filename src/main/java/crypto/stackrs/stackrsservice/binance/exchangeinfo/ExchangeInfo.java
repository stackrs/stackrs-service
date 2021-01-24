package crypto.stackrs.stackrsservice.binance.exchangeinfo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExchangeInfo {
  private List<Symbol> symbols = new ArrayList<>();
}
