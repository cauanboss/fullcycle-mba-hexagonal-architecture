package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import java.util.Objects;

public class Partner {
    private final PartnerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    public Partner(final PartnerId partnerId, final String name, final String cnpj, final String email) {
        if (partnerId == null) {
            throw new ValidationException("PartnerId is required", null);
        }

        this.partnerId = partnerId;
        this.setName(name);
        this.setEmail(email);
        this.setCnpj(cnpj);
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

    public String partnerId() {
        return this.partnerId.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Partner partner = (Partner) o;
        return Objects.equals(partnerId, partner.partnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerId);
    }

    private void setEmail(final String email) {
        this.email = new Email(email);
    }

    private void setCnpj(final String cnpj) {
        this.cnpj = new Cnpj(cnpj);
    }

    private void setName(final String name) {
        this.name = new Name(name);
    }

}
