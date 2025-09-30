package fr.avenirsesr.portfolio.common.data.domain.model;

import fr.avenirsesr.portfolio.common.data.domain.model.enums.EDateFilterKey;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public record DateFilter(LocalDate fromDate, LocalDate toDate) {
  public Map<EDateFilterKey, Object> toMap() {
    var map = new HashMap<EDateFilterKey, Object>();
    if (fromDate != null) {
      map.put(EDateFilterKey.FROM_DATE, fromDate);
    }
    if (toDate != null) {
      map.put(EDateFilterKey.TO_DATE, toDate);
    }
    return map;
  }
}
