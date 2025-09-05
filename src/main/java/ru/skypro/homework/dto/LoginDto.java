package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для авторизации пользователя")
public class LoginDto {

    @Schema(description = "пароль пользователя")
    private String password;

    @Schema(description = "имя пользователя (логин)")
    private String username;
}
