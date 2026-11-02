package cl.duoc.dsy2205.forum.repository;
import cl.duoc.dsy2205.forum.model.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CommentRepository extends JpaRepository<ForumComment,Long>{
    List<ForumComment> findByTopicIdAndStatusOrderByCreatedAtAsc(Long topicId,String status);
    void deleteByTopic_Id(Long topicId);
}
