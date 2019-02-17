package jp.co.soramitsu.seed.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Setter
public class PersonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "first_name")
  private String fistName;

  @Column(name = "last_name")
  private String lastName;
}
