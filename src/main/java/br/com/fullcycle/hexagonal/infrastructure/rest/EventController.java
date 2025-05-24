package br.com.fullcycle.hexagonal.infrastructure.rest;

import static org.springframework.http.HttpStatus.CREATED;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import java.net.URI;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "events")
public class EventController {

  private final CreateEventUseCase createEventUseCase;

  private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

  public EventController(
      CreateEventUseCase createEventUseCase,
      SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase) {
    this.createEventUseCase = Objects.requireNonNull(createEventUseCase);
    this.subscribeCustomerToEventUseCase = Objects.requireNonNull(subscribeCustomerToEventUseCase);
  }

  @PostMapping
  @ResponseStatus(CREATED)
  public ResponseEntity<?> create(@RequestBody NewEventDTO dto) {
    try {
      final var output =
          createEventUseCase.execute(
              new CreateEventUseCase.Input(
                  dto.name(), dto.date(), dto.totalSpots(), dto.partnerId()));
      return ResponseEntity.created(URI.create("/events/" + output.id())).body(output);
    } catch (ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @Transactional
  @PostMapping(value = "/{id}/subscribe")
  public ResponseEntity<?> subscribe(@PathVariable String id, @RequestBody SubscribeDTO dto) {
    try {
      final var input =
          subscribeCustomerToEventUseCase.execute(
              new SubscribeCustomerToEventUseCase.Input(id, dto.customerId()));
      return ResponseEntity.ok(input);
    } catch (ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }
}
