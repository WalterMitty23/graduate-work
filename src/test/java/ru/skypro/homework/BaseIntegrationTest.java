package ru.skypro.homework;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.model.User;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupUsers() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("Test");          // ✅
        user.setLastName("User");           // ✅
        user.setPhone("1111111111");        // ✅
        user.setRole(Role.USER);
        userRepository.save(user);

        User admin = new User();
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setFirstName("Admin");        // ✅
        admin.setLastName("User");          // ✅
        admin.setPhone("2222222222");       // ✅
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }
}
