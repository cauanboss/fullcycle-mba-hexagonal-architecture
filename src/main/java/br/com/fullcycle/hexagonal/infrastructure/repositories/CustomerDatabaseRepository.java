package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

// interface Adapter
@Component
public class CustomerDatabaseRepository implements CustomerRepository {

  private final CustomerJpaRepository customerJpaRepository;

  public CustomerDatabaseRepository(CustomerJpaRepository customerJpaRepository) {
    this.customerJpaRepository = customerJpaRepository;
  }

  @Override
  public Optional<Customer> customerOfId(final CustomerId customerId) {
    return this.customerJpaRepository
        .findById(UUID.fromString(customerId.value()))
        .map(it -> it.toCustomer());
  }

  @Override
  public Optional<Customer> customerOfCPF(final Cpf cpf) {
    return this.customerJpaRepository.findByCpf(cpf.value()).map(it -> it.toCustomer());
  }

  @Override
  public Optional<Customer> customerOfEmail(final Email email) {
    return this.customerJpaRepository.findByEmail(email.value()).map(it -> it.toCustomer());
  }

  @Override
  @Transactional
  public Customer create(final Customer customer) {
    return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
  }

  @Override
  @Transactional
  public Customer update(final Customer customer) {
    return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
  }

  @Override
  @Transactional
  public void deleteAll() {
    this.customerJpaRepository.deleteAll();
  }
}
