package fr.avenirsesr.portfolio.common.language.infrastructure.adapter.filter;

import fr.avenirsesr.portfolio.common.web.infrastructure.context.RequestContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LanguageFilterAspect {

  @PersistenceContext private final EntityManager em;

  @Before("@annotation(DisableLangFilter)")
  public void disableFilter() {
    var session = em.unwrap(Session.class);
    session.disableFilter("langFilter");
  }
}
