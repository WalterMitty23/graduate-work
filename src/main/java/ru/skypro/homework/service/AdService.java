package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.util.List;

public interface AdService {
    AdsDto getAllAds();
    ExtendedAdDto getAdById(Integer id);
    AdDto createAd(CreateOrUpdateAdDto dto, Integer userId);
    AdDto updateAd(Integer id, CreateOrUpdateAdDto dto, String username);
    void deleteAd(Integer id, String username);
}
