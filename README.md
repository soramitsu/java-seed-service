# java-seed-service

This repository contains "example" configuration for services that need:

- to get secrets from vault at service startup ([boostrap-vault.yml](./src/main/resources/bootstrap-vault.yml))
- to get non-secret auto-updatable configuration from consul ([application-consul.yml](./src/main/resources/bootstrap-consul.yml))
- to change multiple database backends ([inmem](./src/main/resources/application-db-inmem.yml)/[postgres](./src/main/resources/application-db-postgres.yml)) 
- to support **cloud** configuration, where you need configured [consul](./src/main/resources/application-consul.yml) for configs and service discovery, [vault](./src/main/resources/bootstrap-vault.yml) to obtain secrets.
- to support **local** + [**novault**](./src/main/resources/bootstrap-novault.yml) + [**noconsul**](./src/main/resources/bootstrap-noconsul.yml) configuration, without external dependencies - no consul / no vault / inmem database 

## Rules

Configuration for every external service MUST be in separate profile:
  - this allows to enable/disable certain services during service startup, just by changing SPRING_PROFILES_ACTIVE environment variable. Example: `SPRING_PROFILES_ACTIVE=local,db-inmem,actuator`.
  - this simplifies navigation for configuration
  - example: [resources](./src/main/resources)
    
    First context is `booststrap` context. 
    It is used to bootstrap application with secrets or configuration fetched from configuration server.
    If vault-config-starter dependency is present, then you MUST define two profiles - one to use with vault, and the other one - to disable it.
    
    Second context is `application` context. 
    It is used to configure actual application.
    Here you can define profiles for databases, actuator, etc.


Use profiles in [tests](./src/test/java/jp/co/soramitsu/seed/ExternalServicesTest.java).


## Walkthrough

### Local setup

```
SPRING_PROFILES_ACTIVE=local,actuator,novault,noconsul ./gradlew bootRun
``` 
**Note**: it is mandatory to disable consul and vault with according profiles, 
because if you include according gradle dependencies, default configuration is 
"enabled" for consul and vault, and context will likely to fail because of missing properties.  
   
   You will see that WelcomeProperties are not initialized:
   ```
   2019-02-19 17:59:21.294  INFO 35686 --- [           main] jp.co.soramitsu.seed.SeedApplication     : ----------------------------------------
   2019-02-19 17:59:21.294  INFO 35686 --- [           main] jp.co.soramitsu.seed.SeedApplication     : WelcomeProperties
   2019-02-19 17:59:21.295  INFO 35686 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         consul: custom.welcome is null
   2019-02-19 17:59:21.295  INFO 35686 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         vault:  custom.name    is null
   2019-02-19 17:59:21.295  INFO 35686 --- [           main] jp.co.soramitsu.seed.SeedApplication     : ----------------------------------------
   ```
   
   Also you can check that by going to http://localhost:8080:
   ```
   null null Active profiles: local, actuator, novault, noconsul
   ```
   
### Cloud setup

Start consul, postgres and vault:
```
docker-compose up -d
```
   
We made 0 configuration changes into our application, lets run it with different set of profiles:

```
SPRING_PROFILES_ACTIVE=consul,vault,actuator,db-postgres ./gradlew bootRun
```

And we can see that out config is still missing. That is because we should set according KV property in consul.

```
2019-02-19 18:15:07.406  INFO 36217 --- [           main] jp.co.soramitsu.seed.SeedApplication     : ----------------------------------------
2019-02-19 18:15:07.406  INFO 36217 --- [           main] jp.co.soramitsu.seed.SeedApplication     : WelcomeProperties
2019-02-19 18:15:07.407  INFO 36217 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         consul: custom.welcome is null
2019-02-19 18:15:07.407  INFO 36217 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         vault:  custom.name    is null
2019-02-19 18:15:07.407  INFO 36217 --- [           main] jp.co.soramitsu.seed.SeedApplication     : ----------------------------------------
```

```
null null Active profiles: consul, vault, actuator, db-postgres
```

#### Consul configuration with auto-refresh 

Leave the spring process running, and execute script `./consul_write_welcome.sh "WELCOME"`. 

With this command we set property `config/example1/custom/welcome` with value `WELCOME`. 
- `config` - prefix
- `example1` - application name
- `custom/welcome` - path for [WelcomeProperties](./src/main/java/jp/co/soramitsu/seed/properties/WelcomeProperties.java) (see annotation @ConfigurationProperties)

Observe the result http://localhost:8080:
```
WELCOME null
Active profiles: consul, vault, actuator, db-postgres
```

Configuration can be changed at run-time. To test this, execute script and go to http://localhost:8080

```bash
~/tools/java-seed-service(master*) » ./consul_write_welcome.sh "Hello, "                                                                                                  bogdan@Bogdans-MacBook-Pro
Success! Data written to: config/example1/custom/welcome
```

```
Hello, null
Active profiles: consul, vault, actuator, db-postgres
```

You can check where that configuration property came from by visiting http://localhost:8080/actuator/env and searching for "Hello"

#### Vault configuration at startup time

Write "name" to Vault with script:
```bash
~/tools/java-seed-service(master*) » ./vault_write_name.sh Bogdan                                                                                                         bogdan@Bogdans-MacBook-Pro
Key              Value
---              -----
created_time     2019-02-19T16:34:43.4721412Z
deletion_time    n/a
destroyed        false
version          1

```

Visit http://localhost:8080 and pay attention that configuration did not change.

Visit http://localhost:8080/actuator/env and pay attention that application did not get update from vault. 

Now, reload application:
```
SPRING_PROFILES_ACTIVE=consul,vault,actuator,db-postgres ./gradlew bootRun
```

Pay attention that after reload we get secret from vault:
```
2019-02-19 18:42:04.670  INFO 36963 --- [           main] jp.co.soramitsu.seed.SeedApplication     : WelcomeProperties
2019-02-19 18:42:04.670  INFO 36963 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         consul: custom.welcome is Hello,
2019-02-19 18:42:04.670  INFO 36963 --- [           main] jp.co.soramitsu.seed.SeedApplication     :         vault:  custom.name    is Bogdan
2019-02-19 18:42:04.670  INFO 36963 --- [           main] jp.co.soramitsu.seed.SeedApplication     : ----------------------------------------
```

Visit http://localhost:8080/actuator/env and observe the result - we get property secret/data/example1 from vault:
```
      "name": "vault:secret/data/example1",
      "properties": {
        "custom.name": {
          "value": "Bogdan"
        }
      }
    },
```

That's it!

**Bonus**: correct way of getting postgres config described in [application-db-postgres.yml](./src/main/resources/application-db-postgres.yml).
