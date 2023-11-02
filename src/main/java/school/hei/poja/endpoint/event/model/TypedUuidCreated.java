package school.hei.poja.endpoint.event.model;

import school.hei.poja.endpoint.event.gen.UuidCreated;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.ToString;

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
