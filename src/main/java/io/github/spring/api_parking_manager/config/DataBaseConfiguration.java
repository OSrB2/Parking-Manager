package io.github.spring.api_parking_manager.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {

  @Value("${spring.datasource.url}")
  String url;
  @Value("${spring.datasource.username}")
  String username;
  @Value("${spring.datasource.password}")
  String password;
  @Value("${spring.datasource.driver-class-name}")
  String driver;

  @Bean
  public DataSource hikariDataSource() {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName(driver);

    config.setMaximumPoolSize(10);
    config.setMinimumIdle(1);
    config.setPoolName("parking-manager-db-pool");
    config.setMaxLifetime(600000);
    config.setConnectionTimeout(100000);
    config.setConnectionTestQuery("select 1");

    return new HikariDataSource(config);
  }
}
