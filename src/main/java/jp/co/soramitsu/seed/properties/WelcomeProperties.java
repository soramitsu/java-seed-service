package jp.co.soramitsu.seed.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@RefreshScope
@Configuration
@Data
@ConfigurationProperties(prefix = "custom")
@Component
public class WelcomeProperties {

  private String welcome;
  private String name;
}
