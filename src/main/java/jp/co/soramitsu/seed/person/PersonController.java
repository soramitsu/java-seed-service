package jp.co.soramitsu.seed.person;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/person")
@RequiredArgsConstructor
public class PersonController {

  private final PersonService personService;

  @GetMapping("/:id")
  public PersonEntity get(@Param(":id") Long id) {
    return personService.get(id);
  }

  @GetMapping
  public List<PersonEntity> getAll() {
    return personService.getAll();
  }

  @DeleteMapping("/:id")
  public void delete(@Param(":id") Long id) {
    personService.delete(id);
  }

  @PostMapping
  public PersonEntity save(@RequestBody Person person) {
    return personService.save(person);
  }
}
