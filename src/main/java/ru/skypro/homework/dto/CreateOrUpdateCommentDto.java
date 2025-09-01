package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Создание или обновление комментария")
public class CreateOrUpdateCommentDto {
    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    private String text;
}
