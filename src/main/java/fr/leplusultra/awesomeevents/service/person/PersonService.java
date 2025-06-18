package fr.leplusultra.awesomeevents.service.person;

import fr.leplusultra.awesomeevents.dto.PersonDTO;
import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.repositories.person.IPersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final IPersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonService(IPersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public int createNew(Person person) {
        person.setCreatedAt(new Date());

        return personRepository.save(person).getId();
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

    public String getQRCodeDataByPerson(Person person) {
        return String.valueOf(person.toString().hashCode()); //TODO discuss how to create qr code data
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public List<Person> findAllByEventId(int eventId){
        return personRepository.findPeopleByEventId(eventId);
    }
}
