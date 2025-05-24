package br.com.fullcycle.hexagonal.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import jakarta.transaction.Transactional;

// interface Adapter
@Component
public class PartnerDatabaseRepository implements PartnerRepository {

    private final PartnerJpaRepository partnerJpaRepository;

    public PartnerDatabaseRepository(PartnerJpaRepository partnerJpaRepository) {
        this.partnerJpaRepository = partnerJpaRepository;
    }

    @Override
    public Optional<Partner> partnerOfId(final PartnerId partnerId) {
        return this.partnerJpaRepository.findById(UUID.fromString(partnerId.value()))
                .map(it -> it.toPartner());
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final Cnpj cnpj) {
        return this.partnerJpaRepository.findByCnpj(cnpj.value())
                .map(it -> it.toPartner());
    }

    @Override
    public Optional<Partner> partnerOfEmail(final Email email) {
        return this.partnerJpaRepository.findByEmail(email.value())
                .map(it -> it.toPartner());
    }

    @Override
    @Transactional
    public Partner create(final Partner partner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(partner))
                .toPartner();
    }

    @Override
    @Transactional
    public Partner update(final Partner partner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(partner))
                .toPartner();
    }

    @Override
    @Transactional
    public void deleteAll() {
        this.partnerJpaRepository.deleteAll();
    }

}
