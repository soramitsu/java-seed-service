package jp.co.soramitsu.seed.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@RefreshScope
@Configuration
@Data
@ConfigurationProperties(prefix = "dynamic")
@Component
public class DynamicNameConfiguration {

  private String name;
}
