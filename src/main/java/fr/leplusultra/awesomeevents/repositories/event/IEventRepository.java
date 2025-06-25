package fr.leplusultra.awesomeevents.repositories.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEventRepository extends JpaRepository<Event, Integer> {
    List<Event> findEventsByUserId(int userId);
}
