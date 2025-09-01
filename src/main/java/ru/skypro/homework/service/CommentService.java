package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {
    CommentsDto getCommentsByAdId(Integer adId);
    CommentDto addComment(Integer adId, Integer userId, CreateOrUpdateCommentDto dto);
    CommentDto updateComment(Integer commentId, CreateOrUpdateCommentDto dto, String username);
    void deleteComment(Integer commentId, String username);

}
