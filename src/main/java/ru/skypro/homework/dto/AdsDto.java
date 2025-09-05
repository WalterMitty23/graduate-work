package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Список объявлений")
public class AdsDto {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema(description = "список объявлений")
    private List<AdDto> results;
}
