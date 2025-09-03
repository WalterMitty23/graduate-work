package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDto> getUserById(Integer id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public UserDto createUser(Register registerDto) {
        User user = userMapper.fromRegister(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // ✅ хэшируем пароль
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setPhone(userDto.getPhone());
                    user.setImage(userDto.getImage());
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDto getCurrentUser(String username) {
        return userRepository.findByEmail(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Override
    public UserDto updateCurrentUser(String username, UserDto userDto) {
        return userRepository.findByEmail(username)
                .map(user -> {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setPhone(userDto.getPhone());
                    user.setImage(userDto.getImage());
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Override
    public void updateUserImage(String username, String imagePath) {
        userRepository.findByEmail(username)
                .ifPresent(user -> {
                    user.setImage(imagePath);
                    userRepository.save(user);
                });
    }

    @Override
    public boolean changePassword(String username, NewPassword newPassword) {
        return userRepository.findByEmail(username)
                .filter(user -> passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
