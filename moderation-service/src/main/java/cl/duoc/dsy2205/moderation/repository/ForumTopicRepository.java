package cl.duoc.dsy2205.moderation.repository;

import cl.duoc.dsy2205.moderation.model.ForumTopic;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {
    @EntityGraph(attributePaths = {"author", "category"})
    List<ForumTopic> findAllByOrderByCreatedAtDesc();
}
