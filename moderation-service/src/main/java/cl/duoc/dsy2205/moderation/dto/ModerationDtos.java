package cl.duoc.dsy2205.moderation.dto;

import java.time.LocalDateTime;

public final class ModerationDtos {
    private ModerationDtos() {}

    public record TopicResponse(
        Long id,
        String title,
        String body,
        String status,
        Long categoryId,
        String category,
        Long authorId,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long replies
    ) {}

    public record CommentResponse(
        Long id,
        String body,
        String status,
        String author,
        Long topicId,
        LocalDateTime createdAt
    ) {}
}
