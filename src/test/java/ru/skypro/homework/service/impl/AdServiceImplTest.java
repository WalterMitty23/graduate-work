package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdMapper adMapper;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void getExtendedAd_shouldReturnDto() {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Test ad");

        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(1);
        dto.setTitle("Test ad");

        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(adMapper.toExtendedAdDto(ad)).thenReturn(dto);

        ExtendedAdDto result = adService.getExtendedAd(1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test ad");
    }

    @Test
    void addAd_shouldReturnDto() throws IOException {
        ReflectionTestUtils.setField(adService, "photoPath", "build/test-photos");

        CreateOrUpdateAdDto createDto = new CreateOrUpdateAdDto();
        createDto.setTitle("New ad");

        User user = new User();
        user.setId(42);
        user.setEmail("test@test.com");

        Ad ad = new Ad();
        ad.setPk(100);
        ad.setTitle("New ad");
        ad.setUser(user);

        AdDto adDto = new AdDto();
        adDto.setPk(100);
        adDto.setTitle("New ad");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(adMapper.toEntity(createDto)).thenReturn(ad);

        when(adRepository.save(any(Ad.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(adMapper.toAdDto(any(Ad.class))).thenReturn(adDto);

        MockMultipartFile file = new MockMultipartFile(
                "image", "test.png", "image/png", "fake-image".getBytes()
        );

        AdDto result = adService.addAd(createDto, file, auth);

        assertThat(result.getPk()).isEqualTo(100);
        assertThat(result.getTitle()).isEqualTo("New ad");
    }

    @Test
    void updateAd_shouldUpdateFields() {
        Ad ad = new Ad();
        ad.setPk(123);
        ad.setTitle("Old title");
        ad.setDescription("Old desc");
        ad.setPrice(10);

        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("New title");
        dto.setDescription("New desc");
        dto.setPrice(999);

        Ad updated = new Ad();
        updated.setPk(123);
        updated.setTitle("New title");
        updated.setDescription("New desc");
        updated.setPrice(999);

        AdDto resultDto = new AdDto();
        resultDto.setPk(123);
        resultDto.setTitle("New title");

        when(adRepository.findById(123)).thenReturn(Optional.of(ad));
        when(adRepository.save(ad)).thenReturn(updated);
        when(adMapper.toAdDto(updated)).thenReturn(resultDto);

        Authentication auth = mock(Authentication.class);

        AdDto result = adService.updateAd(123, dto, auth);

        assertThat(result.getPk()).isEqualTo(123);
        assertThat(result.getTitle()).isEqualTo("New title");
    }

    @Test
    void deleteAd_shouldDeleteAdAndComments() {
        Ad ad = new Ad();
        ad.setPk(99);

        Comment comment = new Comment();
        comment.setPk(1);
        comment.setAd(ad);

        when(adRepository.existsById(99)).thenReturn(true);
        when(adRepository.findById(99)).thenReturn(Optional.of(ad));
        when(commentRepository.findByAdPk(99)).thenReturn(List.of(comment));

        Authentication auth = mock(Authentication.class);

        adService.deleteAd(99, auth);

        verify(commentRepository).delete(comment);
        verify(adRepository).delete(ad);
    }

    @Test
    void deleteAd_shouldThrowIfNotExists() {
        when(adRepository.existsById(123)).thenReturn(false);

        Authentication auth = mock(Authentication.class);

        assertThrows(AdNotFoundException.class, () -> adService.deleteAd(123, auth));
    }
}
