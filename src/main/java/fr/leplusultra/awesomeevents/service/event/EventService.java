package fr.leplusultra.awesomeevents.service.event;

import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.repositories.event.IEventRepository;
import fr.leplusultra.awesomeevents.repositories.user.IUserRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public EventService(IEventRepository eventRepository, IUserRepository userRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(int id) {
        return eventRepository.findById(id).get();
    }

    @Transactional
    public int createNew(Event event) {
        event.setCreatedAt(new Date());
        event.setUser(userRepository.findAll().get(0)); //TODO Change to match real user
        return eventRepository.save(event).getId();
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }

    public Event convertToEvent(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    public EventDTO convertToEventDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }
}
