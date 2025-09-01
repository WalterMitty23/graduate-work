package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword,
                                            Authentication authentication) {
        boolean changed = userService.changePassword(authentication.getName(), newPassword);
        return changed ? ResponseEntity.ok().build() : ResponseEntity.status(403).build();
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUser updateUser,
                                              Authentication authentication) {
        UserDto dto = new UserDto();
        dto.setFirstName(updateUser.getFirstName());
        dto.setLastName(updateUser.getLastName());
        dto.setPhone(updateUser.getPhone());
        return ResponseEntity.ok(userService.updateCurrentUser(authentication.getName(), dto));
    }

    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile file,
                                                Authentication authentication) {
        String path = "/images/" + file.getOriginalFilename();
        userService.updateUserImage(authentication.getName(), path);
        return ResponseEntity.ok().build();
    }
}
