package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления")
    public ResponseEntity<CommentsDto> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getCommentsByAdId(id));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer id,
                                                 @RequestParam Integer userId,
                                                 @RequestBody CreateOrUpdateCommentDto commentDto) {
        return ResponseEntity.ok(commentService.addComment(id, userId, commentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId,
                                              Authentication authentication) {
        commentService.deleteComment(commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDto dto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto, authentication.getName()));
    }
}
