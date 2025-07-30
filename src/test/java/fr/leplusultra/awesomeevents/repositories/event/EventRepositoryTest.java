package fr.leplusultra.awesomeevents.repositories.event;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.user.IUserRepository;
import fr.leplusultra.awesomeevents.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class EventRepositoryTest {

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveEventWithUser() {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("first@example.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.NETWORK_OWNER);

        User savedUser = userRepository.save(user);

        Event event = new Event();
        event.setName("eventName");
        event.setCreatedAt(LocalDateTime.now());
        event.setPlace("locationName");
        event.setCreatedAt(LocalDateTime.now());
        event.setUser(savedUser);

        Event savedEvent = eventRepository.save(event);

        assertThat(savedEvent.getId()).isGreaterThan(0);
        assertThat(savedEvent.getUser().getId()).isEqualTo(savedUser.getId());

        List<Event> events = eventRepository.findEventsByUserId(savedUser.getId());
        assertNotNull(events);
        assertThat(events).hasSize(1);
    }
}
