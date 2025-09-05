package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для регистрации пользователя")
public class RegisterDto {

    @Schema(description = "имя пользователя (логин)")
    private String username;

    @Schema(description = "пароль пользователя")
    private String password;

    @Schema(description = "имя")
    private String firstName;

    @Schema(description = "фамилия")
    private String lastName;

    @Schema(description = "телефон")
    private String phone;

    @Schema(description = "роль пользователя")
    private Role role;
}
