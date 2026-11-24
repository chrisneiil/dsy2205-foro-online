package cl.duoc.dsy2205.forum.controller;

import cl.duoc.dsy2205.forum.dto.ForumDtos.CategoryActivity;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CategoryResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CommentRequest;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CommentResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicRequest;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicSearchResponse;
import cl.duoc.dsy2205.forum.service.ForumService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForumController {
    private final ForumService service;

    public ForumController(ForumService service) {
        this.service = service;
    }

    @GetMapping("/api/categories")
    public List<CategoryResponse> categories() {
        return service.categories();
    }

    @GetMapping("/api/topics")
    public List<TopicResponse> topics() {
        return service.allActiveTopics();
    }

    @GetMapping("/api/topics/search")
    public List<TopicSearchResponse> search(
        @RequestParam(required = false) String term,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long authorId
    ) {
        return service.search(term, categoryId, authorId);
    }

    @GetMapping("/api/reports/categories")
    public List<CategoryActivity> categoryActivity() {
        return service.categoryActivity();
    }

    @GetMapping("/api/topics/{id}")
    public TopicResponse topic(@PathVariable Long id) {
        return service.topic(id);
    }

    @PostMapping("/api/topics")
    @ResponseStatus(HttpStatus.CREATED)
    public TopicResponse createTopic(@Valid @RequestBody TopicRequest request, Authentication authentication) {
        return service.createTopic(request, authentication.getName());
    }

    @PutMapping("/api/topics/{id}")
    public TopicResponse updateTopic(
        @PathVariable Long id,
        @Valid @RequestBody TopicRequest request,
        Authentication authentication
    ) {
        return service.updateTopic(id, request, authentication.getName(), isAdmin(authentication));
    }

    @DeleteMapping("/api/topics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id, Authentication authentication) {
        service.deleteTopic(id, authentication.getName(), isAdmin(authentication));
    }

    @GetMapping("/api/topics/{id}/comments")
    public List<CommentResponse> comments(@PathVariable Long id) {
        return service.commentsFor(id);
    }

    @PostMapping("/api/topics/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(
        @PathVariable Long id,
        @Valid @RequestBody CommentRequest request,
        Authentication authentication
    ) {
        return service.createComment(id, request, authentication.getName());
    }

    @PutMapping("/api/comments/{id}")
    public CommentResponse updateComment(
        @PathVariable Long id,
        @Valid @RequestBody CommentRequest request,
        Authentication authentication
    ) {
        return service.updateComment(id, request, authentication.getName(), isAdmin(authentication));
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id, Authentication authentication) {
        service.deleteComment(id, authentication.getName(), isAdmin(authentication));
    }

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
