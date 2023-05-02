package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

import java.util.HashSet;
import java.util.Set;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItemId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
        return commentDto;
    }

    public static Set<CommentDto> toCommentDto(Iterable<Comment> comments) {
        Set<CommentDto> result = new HashSet<>();
        if (comments == null) return result;
        for (Comment comment : comments) {
            result.add(toCommentDto(comment));
        }
        return result;
    }
}
