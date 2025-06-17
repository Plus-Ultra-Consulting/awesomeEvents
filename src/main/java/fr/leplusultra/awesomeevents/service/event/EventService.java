package fr.leplusultra.awesomeevents.service.event;

import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.event.IEventRepository;
import fr.leplusultra.awesomeevents.repositories.user.IUserRepository;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public int createNew(Event event, int userId) {
        event.setCreatedAt(new Date());
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()){
            throw new UserException("Authenticated user not found");
        }

        event.setUser(user.get());
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

    public List<Event> findAllByUserId(int userId){
        return eventRepository.findEventsByUserId(userId);
    }
}
