package cl.duoc.dsy2205.forum.repository;
import cl.duoc.dsy2205.forum.model.ForumUserRef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ForumUserRepository extends JpaRepository<ForumUserRef,Long>{ Optional<ForumUserRef> findByUsername(String username); }
