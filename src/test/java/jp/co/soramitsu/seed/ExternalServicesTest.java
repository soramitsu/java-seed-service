package jp.co.soramitsu.seed;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.soramitsu.seed.properties.WelcomeProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExternalServicesTest {

  @Autowired
  private WelcomeProperties welcomeProperties;

  @Test
  @Profile("vault")
  public void vaultShouldHaveConfig() {
    System.out.println("config from vault: name=" + welcomeProperties.getName());
    assertThat(welcomeProperties.getName()).isNotEmpty();
  }

  @Test
  @Profile("consul")
  public void consulShouldHaveConfig() {
    System.out.println("config from consul: welcome=" + welcomeProperties.getWelcome());
    assertThat(welcomeProperties.getWelcome()).isNotEmpty();
  }
}

