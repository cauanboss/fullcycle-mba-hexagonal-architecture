package br.com.fullcycle.hexagonal.application.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException(final String message, final Throwable cause) {
    super(message, cause, true, false);
  }
}
