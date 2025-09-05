package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void getComments_shouldReturnCommentsDto_whenAdExists() {
        Comment comment = new Comment();
        comment.setPk(1);
        comment.setText("Hello!");

        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(1);
        commentsDto.setResults(List.of(new CommentDto()));

        when(adRepository.existsById(99)).thenReturn(true);
        when(commentRepository.findByAdPk(99)).thenReturn(List.of(comment));
        when(commentMapper.toCommentsDto(List.of(comment))).thenReturn(commentsDto);

        CommentsDto result = commentService.getComments(99, mock(Authentication.class));

        assertThat(result.getCount()).isEqualTo(1);
    }

    @Test
    void getComments_shouldThrow_whenAdNotExists() {
        when(adRepository.existsById(100)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> commentService.getComments(100, mock(Authentication.class)));
    }

    @Test
    void addComment_shouldSaveAndReturnDto() {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("New comment");

        User user = new User();
        user.setId(1);
        user.setEmail("test@test.com");

        Ad ad = new Ad();
        ad.setPk(10);

        Comment comment = new Comment();
        comment.setPk(55);
        comment.setText("New comment");

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(55);
        commentDto.setText("New comment");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        when(adRepository.existsById(10)).thenReturn(true);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(adRepository.findById(10)).thenReturn(Optional.of(ad));
        when(commentMapper.toEntity(dto)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.addComment(10, dto, auth);

        assertThat(result.getPk()).isEqualTo(55);
        assertThat(result.getText()).isEqualTo("New comment");
    }

    @Test
    void addComment_shouldThrow_whenAdNotExists() {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        Authentication auth = mock(Authentication.class);

        when(adRepository.existsById(77)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> commentService.addComment(77, dto, auth));
    }

    @Test
    void updateComment_shouldUpdateAndReturnDto() {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("Updated text");

        Comment comment = new Comment();
        comment.setPk(99);
        comment.setText("Old text");

        Comment updated = new Comment();
        updated.setPk(99);
        updated.setText("Updated text");

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(99);
        commentDto.setText("Updated text");

        when(commentRepository.existsById(99)).thenReturn(true);
        when(commentRepository.findById(99)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(updated);
        when(commentMapper.toCommentDto(updated)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(1, 99, dto, mock(Authentication.class));

        assertThat(result.getText()).isEqualTo("Updated text");
    }

    @Test
    void updateComment_shouldThrow_whenNotExists() {
        when(commentRepository.existsById(123)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> commentService.updateComment(1, 123, new CreateOrUpdateCommentDto(), mock(Authentication.class)));
    }

    @Test
    void deleteComment_shouldDelete_whenExists() {
        Comment comment = new Comment();
        comment.setPk(5);

        when(commentRepository.existsById(5)).thenReturn(true);
        when(commentRepository.findById(5)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1, 5, mock(Authentication.class));

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_shouldThrow_whenNotExists() {
        when(commentRepository.existsById(5)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> commentService.deleteComment(1, 5, mock(Authentication.class)));
    }
}
