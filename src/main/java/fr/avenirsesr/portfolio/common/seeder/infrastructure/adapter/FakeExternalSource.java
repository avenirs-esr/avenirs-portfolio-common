package fr.avenirsesr.portfolio.common.seeder.infrastructure.adapter;

import fr.avenirsesr.portfolio.common.seeder.domain.port.output.SharedDataGenerator;
import fr.avenirsesr.portfolio.common.seeder.infrastructure.adapter.data.DataGeneratorProvider;

public class FakeExternalSource {
  private static final DataGeneratorProvider<SharedDataGenerator> dataGenerator =
      new DataGeneratorProvider<SharedDataGenerator>()
          .init(FakeExternalSource.class, SharedDataGenerator.class);

  public static String generateExternalSourceId() {
    int externalIdType = dataGenerator.with("type").number(0, 2);
    return switch (externalIdType) {
      case 0 -> dataGenerator.with("uuid").uuid().toString();
      case 1 -> String.valueOf(dataGenerator.with("number").number(1, 999_999));
      case 2 -> dataGenerator.with("mixe").regexify("[A-Z]{3}[0-9]{3}");
      default -> throw new IllegalStateException("Unexpected value: " + externalIdType);
    };
  }
}
