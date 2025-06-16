package fr.leplusultra.awesomeevents.service.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.repositorie.event.IEventRepository;
import fr.leplusultra.awesomeevents.repositorie.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;

    @Autowired
    public EventService(IEventRepository eventRepository, IUserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(int id) {
        return eventRepository.findById(id).get();
    }

    @Transactional
    public void createNew(Event event) {
        event.setCreatedAt(new Date());
        eventRepository.save(event);
    }
}
