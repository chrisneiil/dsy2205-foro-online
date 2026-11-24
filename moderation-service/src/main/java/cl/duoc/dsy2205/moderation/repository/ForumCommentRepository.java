package cl.duoc.dsy2205.moderation.repository;

import cl.duoc.dsy2205.moderation.model.ForumComment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {
    @EntityGraph(attributePaths = {"author", "topic"})
    List<ForumComment> findAllByOrderByCreatedAtDesc();

    long countByTopic_IdAndStatus(Long topicId, String status);
}
