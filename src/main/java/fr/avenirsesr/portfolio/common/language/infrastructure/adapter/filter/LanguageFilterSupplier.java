package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.filter;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.web.infrastructure.context.RequestContext;
import java.util.function.Supplier;

public class LanguageFilterSupplier implements Supplier<Object> {

  @Override
  public Object get() {
    var requestData = RequestContext.get();
    if (requestData == null) {
      return ELanguage.FALLBACK.name();
    }
    return requestData.preferredLanguage().name();
  }
}
