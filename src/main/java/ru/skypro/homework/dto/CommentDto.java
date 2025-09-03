package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Комментарий")
public class CommentDto {
    @Schema(description = "id автора комментария")
    private Integer author;

    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария (мс с 01.01.1970)")
    private Long createdAt;

    @Schema(description = "id комментария")
    private Integer pk;

    @Schema(description = "текст комментария")
    private String text;
}
