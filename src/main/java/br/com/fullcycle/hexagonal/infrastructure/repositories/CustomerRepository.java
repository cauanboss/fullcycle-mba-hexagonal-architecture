package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

  Optional<Customer> findByCpf(String cpf);

  Optional<Customer> findByEmail(String email);
}
