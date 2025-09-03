package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Override
    public AdsDto getAllAds() {
        List<AdDto> ads = adRepository.findAll()
                .stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return new AdsDto(ads.size(), ads);
    }

    @Override
    public ExtendedAdDto getAdById(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::toExtendedDto)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdDto dto, Integer userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Ad ad = adMapper.fromCreateDto(dto);
        ad.setAuthor(author);
        Ad saved = adRepository.save(ad);
        return adMapper.toDto(saved);
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto dto, String username) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!ad.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not allowed to update this ad");
        }

        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());
        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    public void deleteAd(Integer id, String username) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!ad.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not allowed to delete this ad");
        }

        adRepository.delete(ad);
    }

}
