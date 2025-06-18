package fr.leplusultra.awesomeevents.repositories.person;

import fr.leplusultra.awesomeevents.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
    List<Person> findPeopleByEventId(int eventId);
}
