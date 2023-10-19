package school.hei.poja.endpoint.rest.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.poja.repository.DummyRepository;
import school.hei.poja.repository.model.Dummy;

import java.util.List;

@RestController
@Value
public class HealthController {

  DummyRepository dummyRepository;

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/dummy-table")
  public List<Dummy> dummyTable() {
    return dummyRepository.findAll();
  }
}
