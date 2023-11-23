package school.hei.poja.endpoint.rest.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class EvenController {

  @GetMapping("/even")
  public int getEvenNumber() {
    int n = (int) (Math.random() * Integer.MAX_VALUE);
    return n % 2 == 0 ? (int) (Math.random() * Integer.MAX_VALUE) : (int) (Math.random() * Integer.MAX_VALUE) + 1;
  }
}