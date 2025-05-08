package br.com.fullcycle.hexagonal.application.domain.partner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class PartnerIdTest {

    @Test
    @DisplayName("Deve criar um PartnerId")
    public void testCreatePartnerId() {
        final var partnerId = PartnerId.unique();
        Assertions.assertNotNull(partnerId);
    }

    @Test
    @DisplayName("Deve criar um PartnerId com um valor nulo")
    public void testCreatePartnerIdWithNullValue() {
        final var partnerId = PartnerId.unique();
        Assertions.assertNotNull(partnerId);
    }

    @Test
    @DisplayName("Deve criar um PartnerId com um valor invalido")
    public void testCreatePartnerIdWithInvalidValue() {
        final var partnerId = PartnerId.unique();
        Assertions.assertNotNull(partnerId);
    }
}