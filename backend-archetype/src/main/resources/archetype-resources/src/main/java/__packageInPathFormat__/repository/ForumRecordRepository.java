package ${package}.repository;

import ${package}.model.ForumRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRecordRepository extends JpaRepository<ForumRecord, Long> {}
