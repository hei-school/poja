package school.hei.poja.endpoint.rest.controller;

import static java.io.File.createTempFile;
import static java.util.UUID.randomUUID;

import school.hei.poja.PojaGenerated;
import school.hei.poja.endpoint.event.EventProducer;
import school.hei.poja.endpoint.event.gen.UuidCreated;
import school.hei.poja.file.BucketComponent;
import school.hei.poja.repository.DummyRepository;
import school.hei.poja.repository.DummyUuidRepository;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@Value
public class HealthController {

  DummyRepository dummyRepository;
  DummyUuidRepository dummyUuidRepository;
  EventProducer eventProducer;
  BucketComponent bucketComponent;

  public static final ResponseEntity<String> OK = new ResponseEntity<>("OK", HttpStatus.OK);
  public static final ResponseEntity<String> KO =
      new ResponseEntity<>("KO", HttpStatus.INTERNAL_SERVER_ERROR);

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/health/db")
  public ResponseEntity<String> dummyTable_should_not_be_empty() {
    return dummyRepository.findAll().isEmpty() ? KO : OK;
  }

  @GetMapping(value = "/health/event")
  public ResponseEntity<String> random_uuid_is_fired_then_created() throws InterruptedException {
    var randomUuid = randomUUID().toString();
    var event = new UuidCreated().toBuilder().uuid(randomUuid).build();

    eventProducer.accept(List.of(event));

    Thread.sleep(20_000);
    return dummyUuidRepository.findById(randomUuid).map(dummyUuid -> OK).orElse(KO);
  }

  @GetMapping(value = "/health/bucket")
  public ResponseEntity<String> file_can_be_uploaded_then_signed() throws IOException {
    var fileSuffix = ".txt";
    var filePrefix = randomUUID().toString();
    var tmpFile = createTempFile(filePrefix, fileSuffix);
    FileWriter writer = new FileWriter(tmpFile);
    writer.write(randomUUID().toString());
    writer.close();

    var filename = filePrefix + fileSuffix;
    var bucketKey = "health/" + filename;
    bucketComponent.upload(tmpFile, bucketKey);

    return ResponseEntity.of(
        Optional.of(bucketComponent.presign(bucketKey, Duration.ofMinutes(2)).toString()));
  }
}
