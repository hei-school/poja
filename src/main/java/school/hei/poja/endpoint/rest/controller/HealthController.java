package school.hei.poja.endpoint.rest.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.poja.endpoint.event.EventProducer;
import school.hei.poja.endpoint.event.gen.UuidCreated;
import school.hei.poja.endpoint.event.model.TypedUuidCreated;
import school.hei.poja.repository.DummyRepository;
import school.hei.poja.repository.model.Dummy;

import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@Value
public class HealthController {

  DummyRepository dummyRepository;
  EventProducer eventProducer;

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/dummy-table")
  public List<Dummy> dummyTable() {
    return dummyRepository.findAll();
  }

  @GetMapping("/uuid-created")
  public TypedUuidCreated uuidCrated() {
    var event = new TypedUuidCreated(new UuidCreated().toBuilder().uuid(randomUUID().toString()).build());
    eventProducer.accept(List.of(event));
    return event;
  }
}
