package jp.co.soramitsu.seed;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  private final Environment environment;

  @Value("${welcome}")
  private String welcomeMessage;

  @Autowired
  public RootController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping("/")
  public String retrieveWelcomeMessage() {
    String joined = String.join(", ", this.environment.getActiveProfiles());
    return String.format(
        "Welcome message: %s\n" +
            "Active profiles: %s",
        welcomeMessage,
        joined
    );
  }
}
