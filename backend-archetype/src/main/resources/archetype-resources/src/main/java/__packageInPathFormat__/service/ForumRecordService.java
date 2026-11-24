package ${package}.service;

import ${package}.model.ForumRecord;
import ${package}.repository.ForumRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumRecordService {
    private final ForumRecordRepository repository;

    public ForumRecordService(ForumRecordRepository repository) {
        this.repository = repository;
    }

    public List<ForumRecord> findAll() { return repository.findAll(); }
    public ForumRecord create(ForumRecord record) { return repository.save(record); }
}
