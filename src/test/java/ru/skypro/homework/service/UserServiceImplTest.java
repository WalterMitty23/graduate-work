package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder; // 👈 добавлен мок

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_shouldReturnUserDto_whenExists() {
        // given
        User user = new User();
        user.setId(1);
        UserDto dto = new UserDto();
        dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        // when
        Optional<UserDto> result = userService.getUserById(1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        verify(userRepository).findById(1);
    }

    @Test
    void createUser_shouldEncodePasswordAndSave() {
        // given
        Register register = new Register();
        register.setUsername("test@mail.com");
        register.setPassword("plain-pass");

        User user = new User();
        User savedUser = new User();
        savedUser.setId(10);

        UserDto dto = new UserDto();
        dto.setId(10);

        when(userMapper.fromRegister(register)).thenReturn(user);
        when(passwordEncoder.encode("plain-pass")).thenReturn("encoded-pass"); // 👈 мокаем encode
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(dto);

        // when
        UserDto result = userService.createUser(register);

        // then
        assertThat(result.getId()).isEqualTo(10);
        verify(passwordEncoder).encode("plain-pass");
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldUpdateFields_whenExists() {
        // given
        User existing = new User();
        existing.setId(5);

        UserDto input = new UserDto();
        input.setFirstName("John");
        input.setLastName("Doe");
        input.setPhone("12345");
        input.setImage("img.png");

        User updated = new User();
        updated.setId(5);
        updated.setFirstName("John");

        UserDto dto = new UserDto();
        dto.setId(5);
        dto.setFirstName("John");

        when(userRepository.findById(5)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(updated);
        when(userMapper.toDto(updated)).thenReturn(dto);

        // when
        UserDto result = userService.updateUser(5, input);

        // then
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(userRepository).save(existing);
    }

    @Test
    void updateUser_shouldThrow_whenNotFound() {
        // given
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> userService.updateUser(99, new UserDto()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void deleteUser_shouldCallRepository() {
        // when
        userService.deleteUser(123);

        // then
        verify(userRepository).deleteById(123);
    }
}
