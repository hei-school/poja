package school.hei.poja.file;

import org.springframework.stereotype.Component;
import school.hei.poja.PojaGenerated;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@PojaGenerated
@Component
public class BucketComponent {

  private final BucketConf bucketConf;
  private final FileTyper fileTyper;

  public BucketComponent(BucketConf bucketConf, FileTyper fileTyper) {
    this.bucketConf = bucketConf;
    this.fileTyper = fileTyper;
  }

  public FileHash upload(File file, String bucketKey) {
    PutObjectRequest request =
        PutObjectRequest.builder()
            .bucket(bucketConf.getBucketName())
            .contentType(fileTyper.apply(file).toString())
            .checksumAlgorithm(ChecksumAlgorithm.SHA256)
            .key(bucketKey)
            .build();

    PutObjectResponse objectResponse =
        bucketConf.getS3Client().putObject(request, RequestBody.fromFile(file));

    waitUntilObjectExists(bucketKey);
    return new FileHash(FileHashAlgorithm.SHA256, objectResponse.checksumSHA256());
  }

  private void waitUntilObjectExists(String bucketKey) {
    ResponseOrException<HeadObjectResponse> responseOrException =
        bucketConf
            .getS3Client()
            .waiter()
            .waitUntilObjectExists(
                HeadObjectRequest.builder()
                    .bucket(bucketConf.getBucketName())
                    .key(bucketKey)
                    .build())
            .matched();
    responseOrException
        .exception()
        .ifPresent(
            throwable -> {
              throw new RuntimeException(throwable);
            });
  }

  public InputStream download(String bucketKey) {
    GetObjectRequest objectRequest =
        GetObjectRequest.builder().bucket(bucketConf.getBucketName()).key(bucketKey).build();
    return bucketConf.getS3Client().getObjectAsBytes(objectRequest).asInputStream();
  }

  public URL presign(String bucketKey, Duration expiration) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketConf.getBucketName()).key(bucketKey).build();
    PresignedGetObjectRequest presignedRequest =
        bucketConf
            .getS3Presigner()
            .presignGetObject(
                GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build());
    return presignedRequest.url();
  }
}
