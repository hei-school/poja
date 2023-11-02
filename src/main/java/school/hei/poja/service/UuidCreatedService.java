package school.hei.poja.service;

import school.hei.poja.endpoint.event.gen.UuidCreated;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class UuidCreatedService implements Consumer<UuidCreated> {

  @Override
  public void accept(UuidCreated uuidCreated) {
    log.info("Asynchronously receive:d uuidCreated={}.", uuidCreated);
  }
}