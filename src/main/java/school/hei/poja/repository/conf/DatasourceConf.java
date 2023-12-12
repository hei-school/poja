package school.hei.poja.repository.conf;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import school.hei.poja.PojaGenerated;

@PojaGenerated
@Configuration
public class DatasourceConf {

  private final String driverClassName;
  private final String appPropDbUrl;
  private final String appProdDbUsername;
  private final String appPropDbPassword;

  public DatasourceConf(
      @Value("${driverClassName:#{null}}") String driverClassName,
      @Value("${spring.datasource.url:#{null}}") String appPropDbUrl,
      @Value("${spring.datasource.username:#{null}}") String appProdDbUsername,
      @Value("${spring.datasource.password:#{null}}") String appPropDbPassword) {
    this.driverClassName = driverClassName;
    this.appPropDbUrl = appPropDbUrl;
    this.appProdDbUsername = appProdDbUsername;
    this.appPropDbPassword = appPropDbPassword;
  }

  @Bean
  @Primary
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .driverClassName(
            driverClassName == null ? System.getenv("DRIVERCLASSNAME") : driverClassName)
        .url(appPropDbUrl == null ? System.getenv("DATABASE_URL") : appPropDbUrl)
        .username(
            appProdDbUsername == null ? System.getenv("DATABASE_USERNAME") : appProdDbUsername)
        .password(
            appPropDbPassword == null ? System.getenv("DATABASE_PASSWORD") : appPropDbPassword)
        .build();
  }
}
