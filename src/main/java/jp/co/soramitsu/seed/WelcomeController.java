package jp.co.soramitsu.seed;

import jp.co.soramitsu.seed.properties.DynamicNameConfiguration;
import jp.co.soramitsu.seed.properties.StaticWelcomeConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

  private final Environment environment;
  private final StaticWelcomeConfiguration staticWelcomeConfiguration;
  private final DynamicNameConfiguration dynamicNameConfiguration;


  @GetMapping("/")
  public String retrieveWelcomeMessage() {
    String joined = String.join(", ", this.environment.getActiveProfiles());

    String welcome = String.format(
        "%s %s",
        staticWelcomeConfiguration.getWelcome(),
        dynamicNameConfiguration.getName()
    );

    return String.format(
        "%s\n" +
            "Active profiles: %s",
        welcome,
        joined
    );
  }
}
