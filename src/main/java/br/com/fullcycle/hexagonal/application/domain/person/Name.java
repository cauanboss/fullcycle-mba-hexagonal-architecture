package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Name(String value) {

  public Name {
    if (value == null || value.isEmpty()) {
      throw new ValidationException("Invalid value for Name", null);
    }
  }
}
