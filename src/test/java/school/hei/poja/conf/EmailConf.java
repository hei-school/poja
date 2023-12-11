package school.hei.poja.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import school.hei.poja.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
