package br.com.fullcycle.hexagonal.infrastructure.jpa.repositories;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventJpaRepository extends CrudRepository<EventEntity, UUID> {

    Optional<EventEntity> findByName(String name);

    Optional<EventEntity> findByDate(LocalDate date);
}
