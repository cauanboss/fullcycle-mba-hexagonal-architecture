package br.com.fullcycle.hexagonal.application.entities;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Partner {
    private PartnerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    public Partner(final PartnerId partnerId, final String name, final String cnpj, final String email) {
        if (partnerId == null) {
            throw new ValidationException("PartnerId is required", null);
        }

        this.partnerId = partnerId;
        this.name = new Name(name);
        this.email = new Email(email);
        this.cnpj = new Cnpj(cnpj);
    }

    public static Partner newPartner(final String name, final String cnpj, final String email) {
        return new Partner(PartnerId.unique(), name, cnpj, email);
    }

    public Email email() {
        return this.email;
    }

    public Cnpj cnpj() {
        return this.cnpj;
    }

    public Name name() {
        return this.name;
    }

    public PartnerId partnerId() {
        return this.partnerId;
    }

}
