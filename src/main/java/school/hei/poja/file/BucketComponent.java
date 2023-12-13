package school.hei.poja.file;

import static java.io.File.createTempFile;

import school.hei.poja.PojaGenerated;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.model.UploadDirectoryRequest;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

@PojaGenerated
@Component
@AllArgsConstructor
public class BucketComponent {

  private final BucketConf bucketConf;

  public FileHash upload(File file, String bucketKey) {
    return file.isDirectory() ? uploadDirectory(file, bucketKey) : uploadFile(file, bucketKey);
  }

  private FileHash uploadDirectory(File file, String bucketKey) {
    var request =
        UploadDirectoryRequest.builder()
            .source(file.toPath())
            .bucket(bucketConf.getBucketName())
            .s3Prefix(bucketKey)
            .build();
    var upload = bucketConf.getS3TransferManager().uploadDirectory(request);
    var uploaded = upload.completionFuture().join();
    if (!uploaded.failedTransfers().isEmpty()) {
      throw new RuntimeException("Failed to upload following files: " + uploaded.failedTransfers());
    }
    return new FileHash(FileHashAlgorithm.NONE, null);
  }

  private FileHash uploadFile(File file, String bucketKey) {
    var request =
        UploadFileRequest.builder()
            .source(file)
            .putObjectRequest(req -> req.bucket(bucketConf.getBucketName()).key(bucketKey))
            .addTransferListener(LoggingTransferListener.create())
            .build();
    var upload = bucketConf.getS3TransferManager().uploadFile(request);
    var uploaded = upload.completionFuture().join();
    return new FileHash(FileHashAlgorithm.SHA256, uploaded.response().checksumSHA256());
  }

  @SneakyThrows
  public File download(String bucketKey) {
    var destination =
        createTempFile(prefixFromBucketKey(bucketKey), suffixFromBucketKey(bucketKey));
    FileDownload download =
        bucketConf
            .getS3TransferManager()
            .downloadFile(
                DownloadFileRequest.builder()
                    .getObjectRequest(
                        GetObjectRequest.builder()
                            .bucket(bucketConf.getBucketName())
                            .key(bucketKey)
                            .build())
                    .destination(destination)
                    .build());
    download.completionFuture().join();
    return destination;
  }

  private String prefixFromBucketKey(String bucketKey) {
    return lastNameSplitByDot(bucketKey)[0];
  }

  private String suffixFromBucketKey(String bucketKey) {
    var splitByDot = lastNameSplitByDot(bucketKey);
    return splitByDot.length == 1 ? "" : splitByDot[splitByDot.length - 1];
  }

  private String[] lastNameSplitByDot(String bucketKey) {
    var splitByDash = bucketKey.split("/");
    var lastName = splitByDash[splitByDash.length - 1];
    return lastName.split("\\.");
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
