package jp.co.soramitsu.seed.person;

import java.util.List;

public interface PersonService {

  PersonEntity save(Person person);

  List<PersonEntity> getAll();

  PersonEntity get(Long id);

  void delete(Long id);
}
