package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    // Ad → AdDto
    @Mapping(source = "id", target = "pk")  // id → pk
    @Mapping(source = "author.id", target = "author")
    AdDto toDto(Ad ad);

    // Ad → ExtendedAd (расширенный вариант для деталей)
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAdDto toExtendedDto(Ad ad);

    // CreateOrUpdateAdDto → Ad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ad fromCreateDto(CreateOrUpdateAdDto dto);
}
