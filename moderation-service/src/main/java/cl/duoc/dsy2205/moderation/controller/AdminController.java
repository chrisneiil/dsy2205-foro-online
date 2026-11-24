package cl.duoc.dsy2205.moderation.controller;

import cl.duoc.dsy2205.moderation.dto.ModerationDtos.CommentResponse;
import cl.duoc.dsy2205.moderation.dto.ModerationDtos.TopicResponse;
import cl.duoc.dsy2205.moderation.service.ModerationService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ModerationService service;

    public AdminController(ModerationService service) {
        this.service = service;
    }

    @GetMapping("/topics")
    public List<TopicResponse> topics() {
        return service.topics();
    }

    @GetMapping("/comments")
    public List<CommentResponse> comments() {
        return service.comments();
    }

    @PutMapping("/topics/{id}/hide")
    public TopicResponse hideTopic(@PathVariable Long id) {
        return service.hideTopic(id);
    }

    @PutMapping("/comments/{id}/hide")
    public void hideComment(@PathVariable Long id) {
        service.hideComment(id);
    }
}
