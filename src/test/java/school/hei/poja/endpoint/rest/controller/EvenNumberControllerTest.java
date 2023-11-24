package test.java.school.hei.poja.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EvenNumberControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void testGetRandomEvenNumber() {
        int randomNumber = restTemplate.getForObject(baseUrl + "/even", Integer.class);
        assertTrue(randomNumber % 2 == 0, "Le nombre renvoyé devrait être pair");
    }
}
