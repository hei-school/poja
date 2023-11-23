package school.hei.poja.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.poja.endpoint.rest.controller.EvenController;
class EvenControllerIT {

  @Autowired HealthController healthController;

  @Test
  void ping() {
    assertEquals("pong", healthController.ping());
  }


  @Test
  public void should_return_even_number() {
    EvenController controller = new EvenController();

    int number = controller.getEvenNumber();

    assertEquals(0, number % 2);
  }

  @Test
  public void shouldReturnDifferentEvenNumbers() {
    EvenController controller = new EvenController();

    int number1 = controller.getEvenNumber();
    int number2 = controller.getEvenNumber();

    assertEquals(0, number1 % 2);
    assertEquals(0, number2 % 2);
    assertNotEquals(number1, number2);
  }

}