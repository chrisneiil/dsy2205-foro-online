package cl.duoc.dsy2205.forum.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="FORUM_COMMENTS")
public class ForumComment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=1000) private String body;
    @Column(nullable=false,length=12) private String status="ACTIVE";
    @Column(name="CREATED_AT",nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="TOPIC_ID",nullable=false) private Topic topic;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="AUTHOR_ID",nullable=false) private ForumUserRef author;
    protected ForumComment(){}
    public ForumComment(String body,Topic topic,ForumUserRef author){this.body=body;this.topic=topic;this.author=author;}
    public Long getId(){return id;} public String getBody(){return body;} public String getStatus(){return status;}
    public LocalDateTime getCreatedAt(){return createdAt;} public Topic getTopic(){return topic;} public ForumUserRef getAuthor(){return author;}
    public void setBody(String body){this.body=body;} public void setStatus(String status){this.status=status;}
}
