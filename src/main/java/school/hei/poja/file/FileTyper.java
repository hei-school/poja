package school.hei.poja.file;

import school.hei.poja.PojaGenerated;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.function.Function;

import static org.springframework.http.MediaType.parseMediaType;

@PojaGenerated
@Component
public class FileTyper implements Function<File, MediaType> {

  @SneakyThrows
  @Override
  public MediaType apply(File file) {
    var tika = new Tika();
    String detectedMediaType = tika.detect(file);
    return parseMediaType(detectedMediaType);
  }
}
