package fr.leplusultra.awesomeevents.repositories.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {
    Optional<List<Event>> findEventsByUserId(int userId);
}
