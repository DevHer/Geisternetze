package de.kurs4.geisternetze.repository;

import de.kurs4.geisternetze.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
