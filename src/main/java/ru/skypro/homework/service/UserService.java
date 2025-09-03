package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.Register;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> getUserById(Integer id);

    UserDto createUser(Register registerDto);

    UserDto updateUser(Integer id, UserDto userDto);

    void deleteUser(Integer id);

    UserDto getCurrentUser(String username);

    UserDto updateCurrentUser(String username, UserDto userDto);

    void updateUserImage(String username, String imagePath);

    boolean changePassword(String username, NewPassword newPassword);
}
