package jp.co.soramitsu.seed;

import jp.co.soramitsu.seed.properties.WelcomeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@EnableConfigurationProperties(WelcomeProperties.class)
@RequiredArgsConstructor
public class SeedApplication implements CommandLineRunner {

  private final WelcomeProperties welcomeProperties;

  public static void main(String[] args) {
    SpringApplication.run(SeedApplication.class, args);
  }

  @Override
  public void run(String... args) {

    log.info("----------------------------------------");
    log.info("WelcomeProperties");
    log.info("        consul: custom.welcome is {}", welcomeProperties.getWelcome());
    log.info("        vault:  custom.name    is {}", welcomeProperties.getName());
    log.info("----------------------------------------");
  }
}

