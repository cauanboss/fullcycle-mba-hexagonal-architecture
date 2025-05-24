package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Cpf(String value) {

  private static final String CPF_PATTERN = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$";

  public Cpf {
    if (value == null || !value.matches(CPF_PATTERN)) {
      throw new ValidationException("Invalid value for Cpf", null);
    }
  }
}
