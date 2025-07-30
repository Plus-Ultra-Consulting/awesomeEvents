package fr.leplusultra.awesomeevents.controller.person;

import com.google.zxing.WriterException;
import fr.leplusultra.awesomeevents.dto.ErrorResponse;
import fr.leplusultra.awesomeevents.dto.PeopleResponse;
import fr.leplusultra.awesomeevents.dto.PersonDTO;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.email.EmailService;
import fr.leplusultra.awesomeevents.service.event.EventService;
import fr.leplusultra.awesomeevents.service.person.PersonService;
import fr.leplusultra.awesomeevents.service.user.UserService;
import fr.leplusultra.awesomeevents.util.Error;
import fr.leplusultra.awesomeevents.util.exception.EventException;
import fr.leplusultra.awesomeevents.util.exception.PersonException;
import fr.leplusultra.awesomeevents.util.exception.UserException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final EventService eventService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public PersonController(PersonService personService, EventService eventService, UserService userService, EmailService emailService) {
        this.personService = personService;
        this.eventService = eventService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Event event = getUserEvent(personDTO.getEventId(), user);

        personDTO.setId(0);
        Person person = personService.prepareNewPerson(personDTO, event, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        int createdPersonId = personService.createNew(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", createdPersonId));
    }

    @GetMapping("/{eventId}/people")
    public PeopleResponse getAll(@PathVariable int eventId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Event event = getUserEvent(eventId, user);

        return new PeopleResponse(personService.findAllByEventId(event.getId()).stream().map(personService::convertToPersonDTO).toList());
    }

    @GetMapping("/{eventId}/{id}")
    public PersonDTO getOne(@PathVariable int eventId, @PathVariable int id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Event event = getUserEvent(eventId, user);
        Person person = getPersonForEvent(id, event);

        return personService.convertToPersonDTO(person);
    }

    @GetMapping("/{eventId}/securityCode/{securityCode}")
    public PersonDTO getOneBySecurityCode(@PathVariable int eventId, @PathVariable String securityCode, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Event event = getUserEvent(eventId, user);
        Person person = personService.findBySecurityCode(securityCode);

        if (person == null || person.getEvent() != event) {
            throw new EventException("Person not found for authenticated user's event");
        }

        return personService.convertToPersonDTO(person);
    }

    @PatchMapping()
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Person person = getValidatedPersonForUser(personDTO.getId(), user);

        personService.updatePerson(person, personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            Error.returnErrorToClient(bindingResult);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> delete(@RequestBody PersonDTO personDTO, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Person person = getValidatedPersonForUser(personDTO.getId(), user);

        personService.deleteByIdAndEventId(person.getId(), person.getEvent().getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sendSecurityCode")
    public ResponseEntity<HttpStatus> sendSecurityCodeMail(@RequestBody PersonDTO personDTO, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Person person = getValidatedPersonForUser(personDTO.getId(), user);

        try {
            emailService.sendEmailWithSecurityCodeToPerson(person);
        } catch (MessagingException | WriterException | IOException e) {
            throw new PersonException("Failed to send the email");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/useSecurityCode")
    public ResponseEntity<HttpStatus> useQRCode(@RequestBody PersonDTO personDTO, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        Person person = validateSecurityCodeForPerson(personDTO.getSecurityCode(), user);

        personService.markQRCodeAsUsed(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        if (user == null) {
            throw new UserException("Authenticated user not found");
        }

        return user;
    }

    private Event getUserEvent(int eventId, User user) {
        Event event = eventService.findById(eventId);

        if (event == null || !event.getUser().equals(user)) {
            throw new EventException("Event not found for authenticated user");
        }

        return event;
    }

    private Person getPersonForEvent(int personId, Event event) {
        Person person = personService.findById(personId);
        if (person == null || !event.equals(person.getEvent())) {
            throw new PersonException("Person not found for this event");
        }

        return person;
    }

    private Person getValidatedPersonForUser(int personId, User user) {
        Person person = personService.findById(personId);

        if (person == null) {
            throw new PersonException("Person not found");
        }

        Event event = person.getEvent();

        if (event == null) {
            throw new EventException("Person is not associated with any event");
        }

        if (!event.getUser().equals(user)) {
            throw new EventException("Event not found for authenticated user");
        }

        return person;
    }

    private Person validateSecurityCodeForPerson(String securityCode, User user) {
        Person person = personService.findBySecurityCode(securityCode);
        if (person == null || !person.getEvent().getUser().equals(user)) {
            throw new PersonException("Security code is invalid");
        }

        return person;
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
