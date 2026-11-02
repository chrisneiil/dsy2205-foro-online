package cl.duoc.dsy2205.forum.controller;

import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicResponse;
import cl.duoc.dsy2205.forum.service.ForumService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController{
    private final ForumService service;public AdminController(ForumService service){this.service=service;}
    @GetMapping("/topics") public List<TopicResponse> allTopics(){return service.allTopicsForAdmin();}
    @PutMapping("/topics/{id}/hide") public TopicResponse hideTopic(@PathVariable Long id){service.moderateTopic(id);return service.topicForAdmin(id);}
    @PutMapping("/comments/{id}/hide") public void hideComment(@PathVariable Long id){service.moderateComment(id);}
}
