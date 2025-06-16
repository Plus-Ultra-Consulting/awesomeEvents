package fr.leplusultra.awesomeevents.repositories.user;

import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.util.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testSaveAndFindUserByEmail() {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("first@example.com");
        user.setCreatedAt(new Date());
        user.setPassword("securePassword123!");
        user.setRole(UserRole.REGULAR);

        User saved = userRepository.save(user);
        assertThat(saved.getId()).isGreaterThan(0);

        Optional<User> found = userRepository.findByEmail("first@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("First");
    }
}