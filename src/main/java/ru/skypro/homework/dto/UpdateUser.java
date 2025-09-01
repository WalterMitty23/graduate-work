package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Обновление пользователя")
public class UpdateUser {
    @Schema(description = "имя", minLength = 3, maxLength = 10)
    private String firstName;

    @Schema(description = "фамилия", minLength = 3, maxLength = 10)
    private String lastName;

    @Schema(description = "телефон", example = "+7(999)123-45-67", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}