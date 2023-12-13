package school.hei.poja.endpoint.rest.controller.health;

import static school.hei.poja.file.FileHashAlgorithm.NONE;
import static java.io.File.createTempFile;
import static java.nio.file.Files.createTempDirectory;
import static java.util.UUID.randomUUID;

import school.hei.poja.PojaGenerated;
import school.hei.poja.file.BucketComponent;
import school.hei.poja.file.FileHash;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@AllArgsConstructor
public class HealthBucketController {

  BucketComponent bucketComponent;

  private static final String HEALTH_KEY = "health/";

  @GetMapping(value = "/health/bucket")
  public ResponseEntity<String> file_can_be_uploaded_then_signed() throws IOException {
    var fileSuffix = ".txt";
    var filePrefix = randomUUID().toString();
    var fileToUpload = createTempFile(filePrefix, fileSuffix);
    writeRandomContent(fileToUpload);
    var fileBucketKey = HEALTH_KEY + filePrefix + fileSuffix;
    can_upload_file_then_download_file(fileToUpload, fileBucketKey);

    var directoryPrefix = "dir-" + randomUUID();
    var directoryToUpload = createTempDirectory(directoryPrefix).toFile();
    var fileInDirectory =
        new File(directoryToUpload.getAbsolutePath() + "/" + randomUUID() + ".txt");
    writeRandomContent(fileInDirectory);
    var directoryBucketKey = HEALTH_KEY + directoryPrefix;
    can_upload_directory(directoryToUpload, directoryBucketKey);

    return ResponseEntity.of(Optional.of(can_presign(fileBucketKey).toString()));
  }

  private void writeRandomContent(File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    var content = randomUUID().toString();
    writer.write(content);
    writer.close();
  }

  private File can_upload_file_then_download_file(File toUpload, String bucketKey)
      throws IOException {
    bucketComponent.upload(toUpload, bucketKey);

    var downloaded = bucketComponent.download(bucketKey);
    var downloadedContent = Files.readString(downloaded.toPath());
    var uploadedContent = Files.readString(toUpload.toPath());
    if (!uploadedContent.equals(downloadedContent)) {
      throw new RuntimeException("Uploaded and downloaded contents mismatch");
    }

    return downloaded;
  }

  private FileHash can_upload_directory(File toUpload, String bucketKey) {
    var hash = bucketComponent.upload(toUpload, bucketKey);
    if (!NONE.equals(hash.algorithm())) {
      throw new RuntimeException("FileHashAlgorithm.NONE expected but got: " + hash.algorithm());
    }
    return hash;
  }

  private URL can_presign(String fileBucketKey) {
    return bucketComponent.presign(fileBucketKey, Duration.ofMinutes(2));
  }
}
