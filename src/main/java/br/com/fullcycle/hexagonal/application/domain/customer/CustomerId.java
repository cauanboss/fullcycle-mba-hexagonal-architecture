package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import java.util.UUID;

public record CustomerId(String value) {

  public CustomerId {
    if (value == null) {
      throw new ValidationException("CustomerId is required", null);
    }
  }

  public static CustomerId unique() {
    return new CustomerId(UUID.randomUUID().toString());
  }

  public static CustomerId with(String value) {
    try {
      return new CustomerId(UUID.fromString(value).toString());
    } catch (IllegalArgumentException e) {
      throw new ValidationException("Invalid value for CustomerId", null);
    }
  }
}
