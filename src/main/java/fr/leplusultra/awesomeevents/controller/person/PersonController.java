package fr.leplusultra.awesomeevents.controller.person;

import fr.leplusultra.awesomeevents.dto.ErrorResponse;
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
import fr.leplusultra.awesomeevents.util.exception.PersonException;
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

    @PostMapping()
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
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdPersonId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}/people")
    public PeopleResponse getAll(@PathVariable int eventId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(eventId);

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for authenticated user");
        }

        return new PeopleResponse(personService.findAllByEventId(event.getId()).stream().map(personService::convertToPersonDTO).toList());
    }

    @GetMapping("/{eventId}/{id}")
    public PersonDTO getOne(@PathVariable int eventId, @PathVariable int id, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Event event = eventService.findById(eventId);
        Person person = personService.findById(id);

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        if (event == null || event.getUser() != user) {
            throw new EventException("Event not found for authenticated user");
        }

        if (person == null || person.getEvent() != event) {
            throw new EventException("Person not found for authenticated user's event");
        }

        return personService.convertToPersonDTO(person);
    }

    @PatchMapping()
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        Person personEdited = personService.convertToPerson(personDTO);

        if (personEdited == null) {
            throw new PersonException("Person not found");
        }

        Person person = personService.findById(personDTO.getId());

        Event event = person.getEvent();

        if (event == null) {
            throw new EventException("Person is not associated with any event");
        }

        if (!event.getUser().equals(user)) {
            throw new EventException("Event not found for authenticated user");
        }

        personValidator.validate(personEdited, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        person.setFirstName(personEdited.getFirstName());
        person.setLastName(personEdited.getLastName());
        person.setEmail(personEdited.getEmail());

        personService.save(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> delete(@RequestBody PersonDTO personDTO, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        Person person = personService.findById(personDTO.getId());

        if (person == null) {
            throw new PersonException("Person not found");
        }

        Event event = person.getEvent();


        if (event == null) {
            throw new EventException("Person is not associated with any event");
        }

        if (!event.getUser().equals(user)) {
            throw new EventException("Event not found for authenticated");
        }

        if (!event.getPeople().contains(person)) {
            throw new PersonException("Person does not belong to the event");
        }

        personService.deleteByIdAndEventId(person.getId(), event.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sendQR")
    public ResponseEntity<HttpStatus> sendQRCode(@RequestBody PersonDTO personDTO, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        Person person = personService.convertToPerson(personDTO);

        if (person == null) {
            throw new PersonException("Person not found");
        }

        Event event = person.getEvent();

        if (event == null) {
            throw new EventException("Person is not associated with any event");
        }

        if (!event.getUser().equals(user)) {
            throw new EventException("Event not found for authenticated");
        }

        personService.getQRCodeDataByPerson(person);
        //#TODO generate QR from data and send to email
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/useQR")
    public ResponseEntity<HttpStatus> useQRCode(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        personService.markQRCodeAsUsed(personService.findById(1)); //#TODO get qr from data | (placeholder)
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleUserException(PersonException personException) {
        ErrorResponse response = new ErrorResponse(personException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
