package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity(name = "Partners")
@Table(name = "partners")
public class PartnerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String cnpj;

  private String email;

  public PartnerEntity() {}

  public PartnerEntity(UUID id, String name, String cnpj, String email) {
    this.id = id;
    this.name = name;
    this.cnpj = cnpj;
    this.email = email;
  }

  public static PartnerEntity of(final Partner partner) {
    return new PartnerEntity(
        UUID.fromString(partner.partnerId()),
        partner.name().value(),
        partner.cnpj().value(),
        partner.email().value());
  }

  public Partner toPartner() {
    return new Partner(PartnerId.with(id.toString()), name, cnpj, email);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
