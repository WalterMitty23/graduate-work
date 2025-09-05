package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для смены пароля")
public class NewPasswordDto {

    @Schema(description = "текущий пароль пользователя")
    private String currentPassword;

    @Schema(description = "новый пароль пользователя")
    private String newPassword;
}
