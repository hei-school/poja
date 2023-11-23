package school.hei.poja.endpoint.rest.controller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EvenControllerTest {

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate;

    @Test
    public void testGetEvenNumber() {
        int result = this.restTemplate.getForObject("http://localhost:" + port + "/even", Integer.class);
        assertTrue(result % 2 == 0);
    }
}