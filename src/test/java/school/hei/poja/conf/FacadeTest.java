package school.hei.poja.conf;

import static java.lang.Runtime.getRuntime;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FacadeTest {

  private static final PostgresTest postgresTest = new PostgresTest();
  private static final EventTest eventTest = new EventTest();

  @BeforeAll
  static void beforeAll() {
    postgresTest.start();
    getRuntime()
        // Do _not_ stop postgresTest in afterAll as it is shared between multiple subclasses of
        // FacadeTest.
        // Doing so might cause some subclasses to stop it while other ones are still using it!
        .addShutdownHook(new Thread(postgresTest::stop));
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    postgresTest.configureProperties(registry);
    eventTest.configureProperties(registry);
  }
}
