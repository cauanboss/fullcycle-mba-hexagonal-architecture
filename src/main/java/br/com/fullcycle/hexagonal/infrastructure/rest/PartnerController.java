package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewPartnerDTO;
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
@RequestMapping(value = "partners")
public class PartnerController {

  private final CreatePartnerUseCase createPartnerUseCase;
  private final GetPartnerByIdUseCase getPartnerByIdUseCase;

  public PartnerController(CreatePartnerUseCase createPartnerUseCase,
      GetPartnerByIdUseCase getPartnerByIdUseCase) {
    this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
    this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody NewPartnerDTO dto) {

    try {
      final var output = createPartnerUseCase.execute(
          new CreatePartnerUseCase.Input(dto.cnpj(), dto.email(), dto.name()));

      return ResponseEntity.created(URI.create("/partners/" + output.id())).body(output);
    } catch (ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable String id) {
    try {
      final var output = getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id));

      if (output != null) {
        return ResponseEntity.ok(output);
      }

      return output.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }
}
