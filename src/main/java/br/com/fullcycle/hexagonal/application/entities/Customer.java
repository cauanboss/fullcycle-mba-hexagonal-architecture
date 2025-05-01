package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Customer {
    private final CustomerId customerId;
    private final Name name;
    private final Cpf cpf;
    private final Email email;

    public Customer(final CustomerId customerId, final String name, final String cpf, final String email) {
        if (customerId == null) {
            throw new ValidationException("CustomerId is required", null);
        }

        this.customerId = customerId;
        this.name = new Name(name);
        this.email = new Email(email);
        this.cpf = new Cpf(cpf);
    }

    public static Customer newCustomer(final String name, final String cpf, final String email) {
        return new Customer(CustomerId.unique(), name, cpf, email);
    }

    public Email email() {
        return this.email;
    }

    public Cpf cpf() {
        return this.cpf;
    }

    public Name name() {
        return this.name;
    }

    public CustomerId customerId() {
        return this.customerId;
    }

}
