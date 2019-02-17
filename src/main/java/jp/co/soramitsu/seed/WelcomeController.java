package jp.co.soramitsu.seed;

import jp.co.soramitsu.seed.properties.WelcomeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

  private final Environment environment;
  private final WelcomeProperties welcomeProperties;


  @GetMapping("/")
  public String retrieveWelcomeMessage() {
    String joined = String.join(", ", this.environment.getActiveProfiles());

    String welcome = String.format(
        "%s %s",
        welcomeProperties.getWelcome(),
        welcomeProperties.getName()
    );

    return String.format(
        "%s\n" +
            "Active profiles: %s",
        welcome,
        joined
    );
  }
}
