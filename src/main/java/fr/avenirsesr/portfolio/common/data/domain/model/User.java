package fr.avenirsesr.portfolio.common.data.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AvenirsBaseModel {
  private String firstName;
  private String lastName;
  private String email;

  private boolean notificationEnabled;

  private UUID acceptedCguId;
  private Instant acceptedCguAt;

  private User(
      UUID id,
      String firstName,
      String lastName,
      String email,
      boolean notificationEnabled,
      UUID acceptedCguId,
      Instant acceptedCguAt,
      Instant createdAt,
      Instant updatedAt) {
    super(id, createdAt, updatedAt);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.notificationEnabled = notificationEnabled;
    this.acceptedCguId = acceptedCguId;
    this.acceptedCguAt = acceptedCguAt;
  }

  public static User create(UUID id, String firstName, String lastName, String email) {
    return new User(id, firstName, lastName, email, false, null, null, null, null);
  }

  public static User toDomain(
      UUID id,
      String firstName,
      String lastName,
      String email,
      boolean notificationEnabled,
      UUID acceptedCguId,
      Instant acceptedCguAt,
      Instant createdAt,
      Instant updatedAt) {
    return new User(
        id,
        firstName,
        lastName,
        email,
        notificationEnabled,
        acceptedCguId,
        acceptedCguAt,
        createdAt,
        updatedAt);
  }
}
