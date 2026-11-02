package cl.duoc.dsy2205.forum.model;

import jakarta.persistence.*;

@Entity @Table(name="FORUM_USERS")
public class ForumUserRef {
    @Id private Long id;
    @Column(nullable=false) private String username;
    @Column(name="USER_ROLE",nullable=false) private String role;
    @Column(nullable=false) private boolean active;
    protected ForumUserRef() {}
    public Long getId(){return id;}
    public String getUsername(){return username;}
    public String getRole(){return role;}
    public boolean isActive(){return active;}
}
