package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdMapper adMapper;

    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void getAdById_shouldReturnExtendedAdDto_whenExists() {
        Ad ad = new Ad();
        ad.setId(1);
        ad.setTitle("Test ad");

        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(1);
        dto.setTitle("Test ad");

        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(adMapper.toExtendedDto(ad)).thenReturn(dto);

        ExtendedAdDto result = adService.getAdById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test ad");
    }

    @Test
    void createAd_shouldReturnAdDto() {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("New ad");

        User user = new User();
        user.setId(1);

        Ad ad = new Ad();
        ad.setTitle("New ad");

        Ad savedAd = new Ad();
        savedAd.setId(10);
        savedAd.setTitle("New ad");

        AdDto adDto = new AdDto();
        adDto.setPk(10);
        adDto.setTitle("New ad");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(adMapper.fromCreateDto(dto)).thenReturn(ad);
        when(adRepository.save(any(Ad.class))).thenReturn(savedAd);
        when(adMapper.toDto(savedAd)).thenReturn(adDto);

        AdDto result = adService.createAd(dto, 1);

        assertThat(result.getPk()).isEqualTo(10);
        assertThat(result.getTitle()).isEqualTo("New ad");
    }

    @Test
    void deleteAd_shouldCallRepository() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@test.com");

        Ad ad = new Ad();
        ad.setId(99);
        ad.setAuthor(user);

        when(adRepository.findById(99)).thenReturn(Optional.of(ad));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        adService.deleteAd(99, "user@test.com");

        verify(adRepository).delete(ad);
    }
}

