package school.hei.poja.endpoint.event;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.poja.conf.FacadeTest;
import school.hei.poja.endpoint.event.gen.UuidCreated;
import school.hei.poja.endpoint.event.model.TypedUuidCreated;
import school.hei.poja.repository.DummyUuidRepository;

class EventConsumerTest extends FacadeTest {

  @Autowired EventConsumer subject;
  @Autowired DummyUuidRepository dummyUuidRepository;

  @Test
  void uuid_created_is_persisted() throws InterruptedException {
    var uuid = randomUUID().toString();

    subject.accept(
        List.of(
            new EventConsumer.AcknowledgeableTypedEvent(
                new TypedUuidCreated(UuidCreated.builder().uuid(uuid).build()), () -> {})));

    Thread.sleep(2_000);
    var saved = dummyUuidRepository.findById(uuid).orElseThrow();
    assertEquals(uuid, saved.getId());
  }
}
