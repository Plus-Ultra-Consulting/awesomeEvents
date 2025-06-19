package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (person.getEmail() == null) {
            errors.rejectValue("email", "", "Email cannot be null");
        }

        if (person.getEvent() == null){
            errors.rejectValue("EventId", "", "EventId cannot be null");
        }

        Person existingPerson = personService.findByEmailAndEventId(person.getEmail(), person.getEvent().getId());

        if (existingPerson != null && existingPerson.getId() != person.getId()) {
            errors.rejectValue("email", "", "Person with this email is already registered for this event");
        }
    }
}