package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Комментарий к объявлению")
public class CommentDto {

    @Schema(description = "id автора комментария")
    private int author;

    @Schema(description = "ссылка на аватар автора")
    private String authorImage;

    @Schema(description = "имя автора комментария")
    private String authorFirstName;

    @Schema(description = "время создания комментария (timestamp)")
    private long createdAt;

    @Schema(description = "id комментария")
    private int pk;

    @Schema(description = "текст комментария")
    private String text;
}
