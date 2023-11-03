package school.hei.poja.endpoint.event.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.ToString;
import school.hei.poja.PojaGenerated;
import school.hei.poja.endpoint.event.gen.UuidCreated;

@PojaGenerated
@AllArgsConstructor
@ToString
public class TypedUuidCreated implements TypedEvent {
  private final UuidCreated uuidCreated;

  @Override
  public String getTypeName() {
    return UuidCreated.class.getTypeName();
  }

  @Override
  public Serializable getPayload() {
    return uuidCreated;
  }
}
