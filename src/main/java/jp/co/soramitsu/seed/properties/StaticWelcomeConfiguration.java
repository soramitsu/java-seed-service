package jp.co.soramitsu.seed.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
@ConfigurationProperties(prefix = "static")
@Component
public class StaticWelcomeConfiguration {

  private String welcome;
}
