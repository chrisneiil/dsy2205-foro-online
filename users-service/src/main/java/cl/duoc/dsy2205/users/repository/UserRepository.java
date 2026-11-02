package cl.duoc.dsy2205.users.repository;

import cl.duoc.dsy2205.users.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("select u.role, u.active, count(u) from UserAccount u group by u.role, u.active order by u.role")
    List<Object[]> summarizeRoles();
}
