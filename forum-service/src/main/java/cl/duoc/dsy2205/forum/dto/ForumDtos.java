package cl.duoc.dsy2205.forum.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public final class ForumDtos {
    private ForumDtos(){}
    public record CategoryResponse(Long id,String name,String description){}
    public record TopicRequest(@NotBlank @Size(max=160) String title,@NotBlank @Size(max=4000) String body,@NotNull Long categoryId){}
    public record TopicResponse(Long id,String title,String body,String status,String category,String author,LocalDateTime createdAt,LocalDateTime updatedAt){}
    public record TopicSearchResponse(Long id,String title,String category,String author,LocalDateTime createdAt,Long commentCount){}
    public record CommentRequest(@NotBlank @Size(max=1000) String body){}
    public record CommentResponse(Long id,String body,String status,String author,Long topicId,LocalDateTime createdAt){}
    public record CategoryActivity(String category,long topics,long comments){}
}
