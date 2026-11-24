package cl.duoc.dsy2205.moderation.service;

import cl.duoc.dsy2205.moderation.dto.ModerationDtos.CommentResponse;
import cl.duoc.dsy2205.moderation.dto.ModerationDtos.TopicResponse;
import cl.duoc.dsy2205.moderation.model.ForumComment;
import cl.duoc.dsy2205.moderation.model.ForumTopic;
import cl.duoc.dsy2205.moderation.repository.ForumCommentRepository;
import cl.duoc.dsy2205.moderation.repository.ForumTopicRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ModerationService {
    private final ForumTopicRepository topics;
    private final ForumCommentRepository comments;

    public ModerationService(ForumTopicRepository topics, ForumCommentRepository comments) {
        this.topics = topics;
        this.comments = comments;
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> topics() {
        return topics.findAllByOrderByCreatedAtDesc().stream().map(this::toTopic).toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> comments() {
        return comments.findAllByOrderByCreatedAtDesc().stream().map(ModerationService::toComment).toList();
    }

    public TopicResponse hideTopic(Long id) {
        ForumTopic topic = topics.findById(id).orElseThrow(() -> notFound("Tema no encontrado"));
        topic.hide();
        return toTopic(topics.save(topic));
    }

    public TopicResponse restoreTopic(Long id) {
        ForumTopic topic = topics.findById(id).orElseThrow(() -> notFound("Tema no encontrado"));
        topic.restore();
        return toTopic(topics.save(topic));
    }

    public void hideComment(Long id) {
        ForumComment comment = comments.findById(id).orElseThrow(() -> notFound("Comentario no encontrado"));
        comment.hide();
        comments.save(comment);
    }

    public void restoreComment(Long id) {
        ForumComment comment = comments.findById(id).orElseThrow(() -> notFound("Comentario no encontrado"));
        comment.restore();
        comments.save(comment);
    }

    private TopicResponse toTopic(ForumTopic topic) {
        return new TopicResponse(
            topic.getId(), topic.getTitle(), topic.getBody(), topic.getStatus(),
            topic.getCategory().getId(), topic.getCategory().getName(),
            topic.getAuthor().getId(), topic.getAuthor().getDisplayName(),
            topic.getCreatedAt(), topic.getUpdatedAt(), comments.countByTopic_IdAndStatus(topic.getId(), "ACTIVE")
        );
    }

    private static CommentResponse toComment(ForumComment comment) {
        return new CommentResponse(
            comment.getId(), comment.getBody(), comment.getStatus(), comment.getAuthor().getDisplayName(),
            comment.getTopic().getId(), comment.getCreatedAt()
        );
    }

    private static ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
