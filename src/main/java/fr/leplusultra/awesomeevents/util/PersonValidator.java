package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.repositories.person.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final IPersonRepository personRepository;

    @Autowired
    public PersonValidator(IPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (person.getEmail() == null) {
            errors.rejectValue("email", "", "Email cannot be null");
        }

        if (person.getEvent() == null || person.getEvent().getId() == 0) {
            errors.rejectValue("eventId", "", "eventId cannot be null");
            return;
        }

        Optional<Person> existingPersonOpt = personRepository.findByEmailAndEventId(person.getEmail(), person.getEvent().getId());

        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();
            if (existingPerson.getId() != person.getId()) {
                errors.rejectValue("email", "", "Person with this email is already registered for this event");
            }
        }
    }
}