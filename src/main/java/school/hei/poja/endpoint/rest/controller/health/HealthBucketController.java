package school.hei.poja.endpoint.rest.controller.health;

import school.hei.poja.PojaGenerated;
import school.hei.poja.file.BucketComponent;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static java.io.File.createTempFile;
import static java.util.UUID.randomUUID;

@PojaGenerated
@RestController
@AllArgsConstructor
public class HealthBucketController {

  BucketComponent bucketComponent;

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
