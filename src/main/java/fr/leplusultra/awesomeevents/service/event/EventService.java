package fr.leplusultra.awesomeevents.service.event;

import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.repositories.event.IEventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final IEventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EventService(IEventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public int createNew(Event event) {
        event.setCreatedAt(LocalDateTime.now());

        return eventRepository.save(event).getId();
    }

    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Transactional
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
