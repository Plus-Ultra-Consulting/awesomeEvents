package fr.leplusultra.awesomeevents.controller.event;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.dto.EventsResponse;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.event.EventService;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.EventValidator;
import fr.leplusultra.awesomeevents.util.exception.EventException;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final EventValidator eventValidator;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, EventValidator eventValidator, UserService userService) {
        this.eventService = eventService;
        this.eventValidator = eventValidator;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        eventDTO.setId(0);

        Event event = eventService.convertToEvent(eventDTO);
        eventValidator.validate(event, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        event.setUser(user);

        int createdEventId = eventService.createNew(event);
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdEventId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public EventsResponse getAll(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        return new EventsResponse(eventService.findAllByUserId(user.getId()).stream().map(eventService::convertToEventDTO).toList());
    }

    @GetMapping("/{id}")
    public EventDTO getOne(@PathVariable int id, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (id == 0){
            throw new EventException("Event Id must be provided");
        }

        Event event = eventService.findById(id);

        if (!event.getUser().equals(user)){
            throw new EventException("Event not found for authenticated user");
        }

        return eventService.convertToEventDTO(event);
    }

    @PatchMapping()
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(eventDTO.getId());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for authenticated user");
        }

        Event eventToEdit = eventService.convertToEvent(eventDTO);
        event.setName(eventToEdit.getName());
        event.setPlace(eventToEdit.getPlace());
        event.setStartAt(eventToEdit.getStartAt());

        eventValidator.validate(event, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        eventService.save(event);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> delete(@RequestBody EventDTO eventDTO, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(eventDTO.getId());

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for current user");
        }

        eventService.deleteById(eventDTO.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleEventException(EventException eventException) {
        ErrorResponse response = new ErrorResponse(eventException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleUserException(UserException userException) {
        ErrorResponse response = new ErrorResponse(userException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
