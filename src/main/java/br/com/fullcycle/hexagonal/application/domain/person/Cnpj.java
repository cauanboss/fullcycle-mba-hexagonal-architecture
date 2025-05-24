package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Cnpj(String value) {

  private static final String CNPJ_PATTERN = "^(\\d{2}[.]?\\d{3}[.]?\\d{3}[/]?\\d{4}[-]?\\d{2})$";

  public Cnpj {
    if (value == null) {
      throw new ValidationException("Cnpj is required", null);
    }
    if (!value.matches(CNPJ_PATTERN)) {
      throw new ValidationException("Invalid value for Cnpj", null);
    }
  }
}
