package cl.duoc.dsy2205.forum.repository;

import cl.duoc.dsy2205.forum.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long>{
    @Query("select t.id as id, t.title as title, c.name as category, u.username as author, t.createdAt as createdAt, count(cm.id) as commentCount " +
        "from Topic t join t.category c join t.author u left join ForumComment cm on cm.topic=t " +
        "where t.status='ACTIVE' and (:term is null or lower(t.title) like lower(concat(concat('%',:term),'%'))) " +
        "and (:categoryId is null or c.id=:categoryId) and (:authorId is null or u.id=:authorId) " +
        "group by t.id,t.title,c.name,u.username,t.createdAt order by t.createdAt desc")
    List<TopicSearchRow> search(@Param("term") String term,@Param("categoryId") Long categoryId,@Param("authorId") Long authorId);
    List<Topic> findByStatusOrderByCreatedAtDesc(String status);
    interface TopicSearchRow { Long getId(); String getTitle(); String getCategory(); String getAuthor(); java.time.LocalDateTime getCreatedAt(); Long getCommentCount(); }
    @Query("select c.name, count(distinct t.id), count(distinct cm.id) from Category c left join Topic t on t.category=c left join ForumComment cm on cm.topic=t group by c.id,c.name order by c.name")
    List<Object[]> categoryActivity();
}
