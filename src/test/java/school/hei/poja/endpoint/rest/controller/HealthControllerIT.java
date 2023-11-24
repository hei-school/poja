package school.hei.poja.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.poja.conf.FacadeIT;

class HealthControllerIT extends FacadeIT {

  @Autowired
  HealthController healthController;

  @Test
  void getEven() {
    int result = healthController.getEven();
    assertEquals(0, result % 2, "The number generated is not even");
  }

  @Test
  void ping() {
    assertEquals("pong", healthController.ping());
  }

  @Test
  void can_read_from_dummy_table() {
    var dummyTableEntries = healthController.dummyTable();
    assertEquals(1, dummyTableEntries.size());
    assertEquals("dummy-table-id-1", dummyTableEntries.get(0).getId());
  }
}
