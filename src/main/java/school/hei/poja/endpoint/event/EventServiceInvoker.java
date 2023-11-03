package school.hei.poja.endpoint.event;

import java.io.Serializable;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.poja.PojaGenerated;
import school.hei.poja.endpoint.event.gen.UuidCreated;
import school.hei.poja.endpoint.event.model.TypedEvent;
import school.hei.poja.service.event.UuidCreatedService;

@PojaGenerated
@AllArgsConstructor
@Component
@Slf4j
public class EventServiceInvoker implements Consumer<TypedEvent> {
  private final UuidCreatedService uuidCreatedService;

  @Override
  public void accept(TypedEvent typedEvent) {
    Serializable payload = typedEvent.getPayload();
    if (UuidCreated.class.getTypeName().equals(typedEvent.getTypeName())) {
      uuidCreatedService.accept((UuidCreated) payload);
    } else {
      log.error("Unexpected type for event={}", typedEvent);
    }
  }
}
