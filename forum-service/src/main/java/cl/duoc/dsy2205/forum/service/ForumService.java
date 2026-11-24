package cl.duoc.dsy2205.forum.service;

import cl.duoc.dsy2205.forum.dto.ForumDtos.CategoryActivity;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CategoryResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CommentRequest;
import cl.duoc.dsy2205.forum.dto.ForumDtos.CommentResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicRequest;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicResponse;
import cl.duoc.dsy2205.forum.dto.ForumDtos.TopicSearchResponse;
import cl.duoc.dsy2205.forum.model.Category;
import cl.duoc.dsy2205.forum.model.ForumComment;
import cl.duoc.dsy2205.forum.model.ForumUserRef;
import cl.duoc.dsy2205.forum.model.Topic;
import cl.duoc.dsy2205.forum.repository.CategoryRepository;
import cl.duoc.dsy2205.forum.repository.CommentRepository;
import cl.duoc.dsy2205.forum.repository.ForumUserRepository;
import cl.duoc.dsy2205.forum.repository.TopicRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ForumService {
    private static final String ACTIVE = "ACTIVE";

    private final CategoryRepository categories;
    private final TopicRepository topics;
    private final CommentRepository comments;
    private final ForumUserRepository users;

    public ForumService(
        CategoryRepository categories,
        TopicRepository topics,
        CommentRepository comments,
        ForumUserRepository users
    ) {
        this.categories = categories;
        this.topics = topics;
        this.comments = comments;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> categories() {
        return categories.findAll().stream()
            .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getDescription()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicSearchResponse> search(String term, Long categoryId, Long authorId) {
        String cleanTerm = term == null || term.isBlank() ? null : term.trim();
        return topics.search(cleanTerm, categoryId, authorId).stream()
            .map(row -> new TopicSearchResponse(
                row.getId(),
                row.getTitle(),
                row.getCategory(),
                row.getAuthor(),
                row.getCreatedAt(),
                row.getCommentCount()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public TopicResponse topic(Long id) {
        Topic topic = findTopic(id);
        ensureActive(topic);
        return toTopicResponse(topic);
    }

    public TopicResponse createTopic(TopicRequest request, String username) {
        ForumUserRef author = findUser(username);
        Category category = findCategory(request.categoryId());
        Topic topic = new Topic(request.title(), request.body(), author, category);
        return toTopicResponse(topics.save(topic));
    }

    public TopicResponse updateTopic(Long id, TopicRequest request, String username, boolean admin) {
        Topic topic = findTopic(id);
        checkOwner(topic.getAuthor().getUsername(), username, admin);
        topic.update(request.title(), request.body(), findCategory(request.categoryId()));
        return toTopicResponse(topics.save(topic));
    }

    public void deleteTopic(Long id, String username, boolean admin) {
        Topic topic = findTopic(id);
        checkOwner(topic.getAuthor().getUsername(), username, admin);
        comments.deleteByTopic_Id(id);
        topics.delete(topic);
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> allActiveTopics() {
        return topics.findByStatusOrderByCreatedAtDesc(ACTIVE).stream()
            .map(this::toTopicResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> commentsFor(Long topicId) {
        Topic topic = findTopic(topicId);
        ensureActive(topic);
        return comments.findByTopicIdAndStatusOrderByCreatedAtAsc(topicId, ACTIVE).stream()
            .map(ForumService::toCommentResponse)
            .toList();
    }

    public CommentResponse createComment(Long topicId, CommentRequest request, String username) {
        Topic topic = findTopic(topicId);
        ensureActive(topic);
        ForumComment comment = new ForumComment(request.body(), topic, findUser(username));
        return toCommentResponse(comments.save(comment));
    }

    public CommentResponse updateComment(Long id, CommentRequest request, String username, boolean admin) {
        ForumComment comment = findComment(id);
        checkOwner(comment.getAuthor().getUsername(), username, admin);
        comment.setBody(request.body());
        return toCommentResponse(comments.save(comment));
    }

    public void deleteComment(Long id, String username, boolean admin) {
        ForumComment comment = findComment(id);
        checkOwner(comment.getAuthor().getUsername(), username, admin);
        comments.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CategoryActivity> categoryActivity() {
        return topics.categoryActivity().stream()
            .map(row -> new CategoryActivity(
                (String) row[0],
                ((Number) row[1]).longValue(),
                ((Number) row[2]).longValue()
            ))
            .toList();
    }

    private ForumUserRef findUser(String username) {
        return users.findByUsername(username)
            .filter(ForumUserRef::isActive)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Cuenta inexistente o inactiva"
            ));
    }

    private Category findCategory(Long id) {
        return categories.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    private Topic findTopic(Long id) {
        return topics.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tema no encontrado"));
    }

    private ForumComment findComment(Long id) {
        return comments.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
    }

    private void ensureActive(Topic topic) {
        if (!ACTIVE.equals(topic.getStatus())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tema no encontrado");
        }
    }

    private void checkOwner(String owner, String currentUser, boolean admin) {
        if (!admin && !owner.equals(currentUser)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Solo el autor o un administrador puede modificar este contenido"
            );
        }
    }

    private TopicResponse toTopicResponse(Topic topic) {
        long replyCount = comments.countByTopic_IdAndStatus(topic.getId(), ACTIVE);
        return new TopicResponse(
            topic.getId(),
            topic.getTitle(),
            topic.getBody(),
            topic.getStatus(),
            topic.getCategory().getId(),
            topic.getCategory().getName(),
            topic.getAuthor().getId(),
            topic.getAuthor().getDisplayName(),
            topic.getCreatedAt(),
            topic.getUpdatedAt(),
            replyCount
        );
    }

    private static CommentResponse toCommentResponse(ForumComment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getBody(),
            comment.getStatus(),
            comment.getAuthor().getDisplayName(),
            comment.getTopic().getId(),
            comment.getCreatedAt()
        );
    }

}
