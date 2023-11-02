package school.hei.poja;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FacadeTest {

  private static final PostgresTest postgresTest = new PostgresTest();
  private static final EventTest eventTest = new EventTest();

  @BeforeAll
  static void beforeAll() {
    postgresTest.start();
  }

  @AfterAll
  static void afterAll() {
    postgresTest.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    postgresTest.configureProperties(registry);
    eventTest.configureProperties(registry);
  }
}
