package jp.co.soramitsu.seed.person;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

  private final PersonRepository repository;

  @Override
  public PersonEntity save(Person person) {
    PersonEntity e = new PersonEntity();
    e.setFistName(person.getFirstName());
    e.setLastName(person.getLastName());
    return this.repository.save(e);
  }

  @Override
  public List<PersonEntity> getAll() {
    return this.repository.findAll();
  }

  @Override
  public PersonEntity get(Long id) {
    return this.repository.findById(id).orElseThrow(() -> new RuntimeException("person not found"));
  }

  @Override
  public void delete(Long id) {
    if (!this.repository.existsById(id)) {
      throw new RuntimeException("person not found");
    }

    this.repository.deleteById(id);
  }
}
