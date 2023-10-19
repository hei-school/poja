package school.hei.poja.endpoint.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.poja.PostgresTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest extends PostgresTest {

  @Autowired
  HealthController healthController;

  @Test
  void ping() {
    assertEquals("pong", healthController.ping());
  }

  @Test
  void dummyTable() {
    var dummyTableEntries = healthController.dummyTable();
    assertEquals(1, dummyTableEntries.size());
    assertEquals("dummy-table-id-1", dummyTableEntries.get(0).getId());
  }
}