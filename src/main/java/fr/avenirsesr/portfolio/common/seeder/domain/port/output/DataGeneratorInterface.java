package fr.avenirsesr.portfolio.common.seeder.domain.port.output;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import java.util.Random;

public interface DataGeneratorInterface {
  Random getRandom();

  void setRandom(Random random);

  ELanguage getLanguage();

  void setLanguage(ELanguage language);
}
