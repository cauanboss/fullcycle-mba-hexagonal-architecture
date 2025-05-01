package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import java.net.URI;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

  private final CreateCustomerUseCase createCustomerUseCase;
  private final GetCustomerByIdUseCase getCustomerByIdUseCase;

  public CustomerController(CreateCustomerUseCase createCustomerUseCase,
      GetCustomerByIdUseCase getCustomerByIdUseCase) {
    this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
    this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
    try {
      final var output = createCustomerUseCase.execute(
          new CreateCustomerUseCase.Input(dto.cpf(), dto.email(), dto.name()));

      return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
    } catch (ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable String id) {
    final var resut = getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id));

    if (resut != null) {
      return ResponseEntity.ok(resut);
    }
    return ResponseEntity.notFound().build();
  }
}
