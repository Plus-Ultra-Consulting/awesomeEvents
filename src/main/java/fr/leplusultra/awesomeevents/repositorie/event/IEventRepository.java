package fr.leplusultra.awesomeevents.repositorie.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {
}
