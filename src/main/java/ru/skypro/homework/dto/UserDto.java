package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Пользователь")
public class UserDto {

    @Schema(description = "id пользователя")
    private int id;

    @Schema(description = "email пользователя")
    private String email;

    @Schema(description = "имя")
    private String firstName;

    @Schema(description = "фамилия")
    private String lastName;

    @Schema(description = "телефон")
    private String phone;

    @Schema(description = "роль пользователя")
    private Role role;

    @Schema(description = "ссылка на аватар пользователя")
    private String image;
}
