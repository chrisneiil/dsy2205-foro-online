package cl.duoc.dsy2205.forum.controller;

import cl.duoc.dsy2205.forum.dto.ForumDtos.*;
import cl.duoc.dsy2205.forum.service.ForumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ForumController{
    private final ForumService service;public ForumController(ForumService service){this.service=service;}
    @GetMapping("/api/categories") public List<CategoryResponse> categories(){return service.categories();}
    @GetMapping("/api/topics") public List<TopicResponse> topics(){return service.allActiveTopics();}
    @GetMapping("/api/topics/search") public List<TopicSearchResponse> search(@RequestParam(required=false) String term,@RequestParam(required=false) Long categoryId,@RequestParam(required=false) Long authorId){return service.search(term,categoryId,authorId);}
    @GetMapping("/api/reports/categories") public List<CategoryActivity> categoryActivity(){return service.categoryActivity();}
    @GetMapping("/api/topics/{id}") public TopicResponse topic(@PathVariable Long id){return service.topic(id);}
    @PostMapping("/api/topics") @ResponseStatus(HttpStatus.CREATED) public TopicResponse createTopic(@Valid @RequestBody TopicRequest req,Authentication auth){return service.createTopic(req,auth.getName());}
    @PutMapping("/api/topics/{id}") public TopicResponse updateTopic(@PathVariable Long id,@Valid @RequestBody TopicRequest req,Authentication auth){return service.updateTopic(id,req,auth.getName(),isAdmin(auth));}
    @DeleteMapping("/api/topics/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteTopic(@PathVariable Long id,Authentication auth){service.deleteTopic(id,auth.getName(),isAdmin(auth));}
    @GetMapping("/api/topics/{id}/comments") public List<CommentResponse> comments(@PathVariable Long id){return service.commentsFor(id);}
    @PostMapping("/api/topics/{id}/comments") @ResponseStatus(HttpStatus.CREATED) public CommentResponse createComment(@PathVariable Long id,@Valid @RequestBody CommentRequest req,Authentication auth){return service.createComment(id,req,auth.getName());}
    @PutMapping("/api/comments/{id}") public CommentResponse updateComment(@PathVariable Long id,@Valid @RequestBody CommentRequest req,Authentication auth){return service.updateComment(id,req,auth.getName(),isAdmin(auth));}
    @DeleteMapping("/api/comments/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteComment(@PathVariable Long id,Authentication auth){service.deleteComment(id,auth.getName(),isAdmin(auth));}
    private static boolean isAdmin(Authentication a){return a.getAuthorities().stream().anyMatch(g->g.getAuthority().equals("ROLE_ADMIN"));}
}
