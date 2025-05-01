package br.com.fullcycle.hexagonal.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fullcycle.hexagonal.infrastructure.Main;

@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
class MainTests {

  @Test
  void contextLoads() {
  }
}
