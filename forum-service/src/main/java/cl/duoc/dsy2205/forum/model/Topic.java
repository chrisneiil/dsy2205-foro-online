package cl.duoc.dsy2205.forum.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="FORUM_TOPICS")
public class Topic {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=160) private String title;
    @Lob @Column(nullable=false) private String body;
    @Column(nullable=false,length=12) private String status="ACTIVE";
    @Column(name="CREATED_AT",nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    @Column(name="UPDATED_AT",nullable=false) private LocalDateTime updatedAt=LocalDateTime.now();
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="AUTHOR_ID",nullable=false) private ForumUserRef author;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="CATEGORY_ID",nullable=false) private Category category;
    protected Topic(){}
    public Topic(String title,String body,ForumUserRef author,Category category){this.title=title;this.body=body;this.author=author;this.category=category;}
    @PreUpdate void touch(){updatedAt=LocalDateTime.now();}
    public Long getId(){return id;} public String getTitle(){return title;} public String getBody(){return body;}
    public String getStatus(){return status;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
    public ForumUserRef getAuthor(){return author;} public Category getCategory(){return category;}
    public void update(String title,String body,Category category){this.title=title;this.body=body;this.category=category;}
    public void setStatus(String status){this.status=status;}
}
