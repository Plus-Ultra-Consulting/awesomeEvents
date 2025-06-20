package fr.leplusultra.awesomeevents.repositories.person;

import fr.leplusultra.awesomeevents.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IPersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByEmailAndEventId(String email, int id);
    List<Person> findAllByEventId(int eventId);
    @Modifying
    @Query("DELETE FROM Person p WHERE p.id = :personId AND p.event.id = :eventId")
    void deleteByIdAndEventId(int personId, int eventId);
}
