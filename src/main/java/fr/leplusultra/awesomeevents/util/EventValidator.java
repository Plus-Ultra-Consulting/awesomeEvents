package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.repositories.event.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {
    private final IEventRepository eventRepository;

    @Autowired
    public EventValidator(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //TODO validate events
    }
}