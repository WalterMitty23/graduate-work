package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void addComment_shouldReturnCommentDto() {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("Hello");

        User user = new User();
        user.setId(1);
        user.setEmail("user@test.com");
        Ad ad = new Ad();
        ad.setId(1);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Hello");

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(1);
        commentDto.setText("Hello");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(commentMapper.fromCreateDto(dto)).thenReturn(new Comment());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.addComment(1, 1, dto);

        assertThat(result.getPk()).isEqualTo(1);
        assertThat(result.getText()).isEqualTo("Hello");
    }

    @Test
    void updateComment_shouldReturnUpdatedCommentDto() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@test.com");

        Comment existing = new Comment();
        existing.setId(5);
        existing.setText("Old");
        existing.setAuthor(user);

        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("New");

        Comment updated = new Comment();
        updated.setId(5);
        updated.setText("New");
        updated.setAuthor(user);

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(5);
        commentDto.setText("New");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(commentRepository.findById(5)).thenReturn(Optional.of(existing));
        when(commentRepository.save(existing)).thenReturn(updated);
        when(commentMapper.toDto(updated)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(5, dto, "user@test.com");

        assertThat(result.getText()).isEqualTo("New");
    }

    @Test
    void deleteComment_shouldDelete_whenExists() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@test.com");

        Comment comment = new Comment();
        comment.setId(42);
        comment.setAuthor(user);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(commentRepository.findById(42)).thenReturn(Optional.of(comment));

        commentService.deleteComment(42, "user@test.com");

        verify(commentRepository).delete(comment);
    }
}
