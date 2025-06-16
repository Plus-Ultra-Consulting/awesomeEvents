package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {
    private final EventService eventService;

    @Autowired
    public EventValidator(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;

        //TODO validate events
    }
}