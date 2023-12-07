package school.hei.poja.file;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import school.hei.poja.PojaGenerated;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@PojaGenerated
@Configuration
public class BucketConf {

  @Getter private final String bucketName;
  @Getter private final S3Client s3Client;
  @Getter private final S3Presigner s3Presigner;

  @SneakyThrows
  public BucketConf(
      @Value("${aws.region}") String regionString, @Value("${aws.s3.bucket}") String bucketName) {
    this.bucketName = bucketName;
    var region = Region.of(regionString);
    this.s3Client = S3Client.builder().region(region).build();
    this.s3Presigner = S3Presigner.builder().region(region).build();
  }
}
