package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentsDto getCommentsByAdId(Integer adId) {
        List<CommentDto> comments = commentRepository.findAll()
                .stream()
                .filter(c -> c.getAd().getId().equals(adId))
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
        return new CommentsDto(comments.size(), comments);
    }

    @Override
    public CommentDto addComment(Integer adId, Integer userId, CreateOrUpdateCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        Comment comment = commentMapper.fromCreateDto(dto);
        comment.setAuthor(author);
        comment.setAd(ad);
        comment.setCreatedAt(System.currentTimeMillis());
        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Override
    public CommentDto updateComment(Integer commentId, CreateOrUpdateCommentDto dto, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!comment.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not allowed to update this comment");
        }

        comment.setText(dto.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!comment.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

}
