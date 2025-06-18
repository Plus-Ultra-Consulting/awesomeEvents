package fr.leplusultra.awesomeevents.controller.person;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.EventDTO;
import fr.leplusultra.awesomeevents.dto.PeopleResponse;
import fr.leplusultra.awesomeevents.dto.PersonDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.event.EventService;
import fr.leplusultra.awesomeevents.service.person.PersonService;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.PersonValidator;
import fr.leplusultra.awesomeevents.util.exception.EventException;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final EventService eventService;
    private final UserService userService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, EventService eventService, UserService userService, PersonValidator personValidator) {
        this.personService = personService;
        this.eventService = eventService;
        this.userService = userService;
        this.personValidator = personValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(personDTO.getEventId());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for authenticated user");
        }

        personDTO.setId(0);
        Person person = personService.convertToPerson(personDTO);
        person.setEvent(event);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        int createdPersonId = personService.createNew(person);
        //#TODO send email to user, bcs he was added
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdPersonId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping()
    public PeopleResponse getAll(@RequestBody EventDTO eventDTO, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(eventDTO.getId());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for authenticated user");
        }

        return new PeopleResponse(personService.findAllByEventId(event.getId()).stream().map(personService::convertToPersonDTO).toList());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleEventException(EventException eventException) {
        ErrorResponse response = new ErrorResponse(eventException.getMessage(), new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleUserException(UserException userException) {
        ErrorResponse response = new ErrorResponse(userException.getMessage(), new Date());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
