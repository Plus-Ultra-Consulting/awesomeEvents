package fr.leplusultra.awesomeevents.repositories.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {
    List<Event> findEventsByUserId(int userId);
}
