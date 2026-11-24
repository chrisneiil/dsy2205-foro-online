package ${package}.controller;

import ${package}.model.ForumRecord;
import ${package}.service.ForumRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class ForumRecordController {
    private final ForumRecordService service;

    public ForumRecordController(ForumRecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<ForumRecord> findAll() { return service.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ForumRecord create(@Valid @RequestBody ForumRecord record) { return service.create(record); }
}
