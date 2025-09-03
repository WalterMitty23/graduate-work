package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService; // твоя реализация AuthService

    @Test
    void login_shouldReturnTrue_whenPasswordMatches() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);

        boolean result = authService.login("test@mail.com", "raw");

        assertThat(result).isTrue();
        verify(passwordEncoder).matches("raw", "encoded");
    }

    @Test
    void login_shouldReturnFalse_whenUserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        boolean result = authService.login("unknown@mail.com", "raw");

        assertThat(result).isFalse();
    }

    @Test
    void register_shouldSaveUserWithEncodedPassword() {
        Register reg = new Register();
        reg.setUsername("new@mail.com");
        reg.setPassword("12345");

        User user = new User();
        user.setEmail("new@mail.com");
        user.setPassword("encoded");

        when(passwordEncoder.encode("12345")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = authService.register(reg);

        assertThat(result).isTrue();
        verify(userRepository).save(any(User.class));
    }
}
