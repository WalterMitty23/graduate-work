package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для создания или обновления объявления")
public class CreateOrUpdateAdDto {

    @Schema(description = "заголовок объявления")
    private String title;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "описание объявления")
    private String description;
}
