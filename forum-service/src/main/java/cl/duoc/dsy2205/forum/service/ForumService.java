package cl.duoc.dsy2205.forum.service;

import cl.duoc.dsy2205.forum.dto.ForumDtos.*;
import cl.duoc.dsy2205.forum.model.*;
import cl.duoc.dsy2205.forum.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @Transactional
public class ForumService{
    private final CategoryRepository categories;private final TopicRepository topics;private final CommentRepository comments;private final ForumUserRepository users;
    public ForumService(CategoryRepository categories,TopicRepository topics,CommentRepository comments,ForumUserRepository users){this.categories=categories;this.topics=topics;this.comments=comments;this.users=users;}
    @Transactional(readOnly=true) public List<CategoryResponse> categories(){return categories.findAll().stream().map(c->new CategoryResponse(c.getId(),c.getName(),c.getDescription())).toList();}
    @Transactional(readOnly=true) public List<TopicSearchResponse> search(String term,Long categoryId,Long authorId){String clean=term==null||term.isBlank()?null:term.trim();return topics.search(clean,categoryId,authorId).stream().map(r->new TopicSearchResponse(r.getId(),r.getTitle(),r.getCategory(),r.getAuthor(),r.getCreatedAt(),r.getCommentCount())).toList();}
    @Transactional(readOnly=true) public TopicResponse topic(Long id){Topic t=findTopic(id);if(!t.getStatus().equals("ACTIVE"))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Tema no encontrado");return topicResponse(t);}
    @Transactional(readOnly=true) public TopicResponse topicForAdmin(Long id){return topicResponse(findTopic(id));}
    public TopicResponse createTopic(TopicRequest req,String username){ForumUserRef user=findUser(username);Category category=findCategory(req.categoryId());return topicResponse(topics.save(new Topic(req.title(),req.body(),user,category)));}
    public TopicResponse updateTopic(Long id,TopicRequest req,String username,boolean admin){Topic t=findTopic(id);checkOwner(t.getAuthor().getUsername(),username,admin);t.update(req.title(),req.body(),findCategory(req.categoryId()));return topicResponse(topics.save(t));}
    public void deleteTopic(Long id,String username,boolean admin){Topic t=findTopic(id);checkOwner(t.getAuthor().getUsername(),username,admin);comments.deleteByTopic_Id(id);topics.delete(t);}
    @Transactional(readOnly=true) public List<TopicResponse> allActiveTopics(){return topics.findByStatusOrderByCreatedAtDesc("ACTIVE").stream().map(ForumService::topicResponse).toList();}
    @Transactional(readOnly=true) public List<CommentResponse> commentsFor(Long topicId){Topic t=findTopic(topicId);if(!t.getStatus().equals("ACTIVE"))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Tema no encontrado");return comments.findByTopicIdAndStatusOrderByCreatedAtAsc(topicId,"ACTIVE").stream().map(ForumService::commentResponse).toList();}
    public CommentResponse createComment(Long topicId,CommentRequest req,String username){Topic t=findTopic(topicId);if(!t.getStatus().equals("ACTIVE"))throw new ResponseStatusException(HttpStatus.CONFLICT,"No se puede comentar un tema oculto");return commentResponse(comments.save(new ForumComment(req.body(),t,findUser(username))));}
    public CommentResponse updateComment(Long id,CommentRequest req,String username,boolean admin){ForumComment c=findComment(id);checkOwner(c.getAuthor().getUsername(),username,admin);c.setBody(req.body());return commentResponse(comments.save(c));}
    public void deleteComment(Long id,String username,boolean admin){ForumComment c=findComment(id);checkOwner(c.getAuthor().getUsername(),username,admin);comments.delete(c);}
    public void moderateTopic(Long id){Topic t=findTopic(id);t.setStatus("HIDDEN");topics.save(t);}
    public void moderateComment(Long id){ForumComment c=findComment(id);c.setStatus("HIDDEN");comments.save(c);}
    @Transactional(readOnly=true) public List<TopicResponse> allTopicsForAdmin(){return topics.findAll().stream().map(ForumService::topicResponse).toList();}
    @Transactional(readOnly=true) public List<CategoryActivity> categoryActivity(){return topics.categoryActivity().stream().map(r->new CategoryActivity((String)r[0],((Number)r[1]).longValue(),((Number)r[2]).longValue())).toList();}
    private ForumUserRef findUser(String name){return users.findByUsername(name).filter(ForumUserRef::isActive).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Cuenta inexistente o inactiva"));}
    private Category findCategory(Long id){return categories.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoría no encontrada"));}
    private Topic findTopic(Long id){return topics.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tema no encontrado"));}
    private ForumComment findComment(Long id){return comments.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Comentario no encontrado"));}
    private static void checkOwner(String owner,String user,boolean admin){if(!admin&&!owner.equals(user))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo el autor o un administrador puede modificar este contenido");}
    private static TopicResponse topicResponse(Topic t){return new TopicResponse(t.getId(),t.getTitle(),t.getBody(),t.getStatus(),t.getCategory().getName(),t.getAuthor().getUsername(),t.getCreatedAt(),t.getUpdatedAt());}
    private static CommentResponse commentResponse(ForumComment c){return new CommentResponse(c.getId(),c.getBody(),c.getStatus(),c.getAuthor().getUsername(),c.getTopic().getId(),c.getCreatedAt());}
}
