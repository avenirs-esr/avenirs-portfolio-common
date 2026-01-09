package fr.avenirsesr.portfolio.common.validation.domain.constraints;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class FieldMaxLengths {

  public static final int TITLE_LENGTH = 80;
  public static final int ORGANIZATION_LENGTH = 50;
  public static final int DESCRIPTION_LENGTH = 400;
  public static final int RESULT_LENGTH = 50;
  public static final int SOURCE_OF_INFORMATION_LENGTH = 200;
  public static final int ACTIVITY_SECTOR_LENGTH = 50;
  public static final int LOCATION_LENGTH = 50;
  public static final int SUMMARY_LENGTH = 400;
  public static final int BIO_LENGTH = 400;
  public static final int AI_JUSTIFICATION_LENGTH = 200;
  public static final int PERSONAL_NOTE_LENGTH = 200;
  public static final int RATING_MIN = 1;
  public static final int RATING_MAX = 5;
}
