package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Расширенное объявление")
public class ExtendedAdDto {

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "имя автора")
    private String authorFirstName;

    @Schema(description = "фамилия автора")
    private String authorLastName;

    @Schema(description = "описание объявления")
    private String description;

    @Schema(description = "email автора")
    private String email;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "телефон автора")
    private String phone;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "заголовок объявления")
    private String title;
}
