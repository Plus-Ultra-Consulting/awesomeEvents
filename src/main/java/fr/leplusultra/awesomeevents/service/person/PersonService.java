package fr.leplusultra.awesomeevents.service.person;

import fr.leplusultra.awesomeevents.dto.PersonDTO;
import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.repositories.person.IPersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
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
        person.setCreatedAt(LocalDateTime.now());

        return personRepository.save(person).getId();
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

    public Person findByEmailAndEventId(String email, int eventId) {
        return personRepository.findByEmail(email).orElse(null);
    }
    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public List<Person> findAllByEventId(int eventId) {
        return personRepository.findAllByEventId(eventId);
    }

    @Transactional
    public void deleteByIdAndEventId(int personId, int eventId) {
        personRepository.deleteByIdAndEventId(personId, eventId);
    }

    public Person findById(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void markQRCodeAsUsed(Person person) {
        person.setSecurityCodeActivatedAt(LocalDateTime.now());
        personRepository.save(person);
    }

    public Person findBySecurityCode(String code){
        return personRepository.findBySecurityCode(code).orElse(null);
    }

    public String generateSecurityCode(){
        String chars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++){
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}
