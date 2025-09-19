package fr.avenirsesr.portfolio.common.seeder.domain.port.output;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DataGenerator implements DataGeneratorInterface {
  private Random random;
  private ELanguage language;
}
