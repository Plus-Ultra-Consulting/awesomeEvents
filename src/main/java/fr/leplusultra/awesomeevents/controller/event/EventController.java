package fr.leplusultra.awesomeevents.controller.event;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.service.event.EventService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.EventValidator;
import fr.leplusultra.awesomeevents.util.exception.EventException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final EventValidator eventValidator;

    @Autowired
    public EventController(EventService eventService, EventValidator eventValidator) {
        this.eventService = eventService;
        this.eventValidator = eventValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult) {
        Event event = eventService.convertToEvent(eventDTO);
        eventValidator.validate(event, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        int createdEventId = eventService.createNew(event);
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdEventId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<EventDTO> getEvents() {
        return eventService.findAll().stream().map(eventService::convertToEventDTO).toList(); //TODO return events of authed user
    }


    @PostMapping("/edit")
    public ResponseEntity<HttpStatus> editEvent(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult) {
        //TODO get current user and update it's event.
        Event event = eventService.convertToEvent(eventDTO);

        eventValidator.validate(event, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        eventService.save(event);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser() {
        //TODO extract current session user and delete it's event
        eventService.deleteById(0);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EventException eventException) {
        ErrorResponse response = new ErrorResponse(eventException.getMessage(), new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
