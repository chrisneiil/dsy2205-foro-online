package cl.duoc.dsy2205.moderation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FORUM_USERS")
public class ForumUser {
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    protected ForumUser() {}

    public Long getId() { return id; }
    public String getDisplayName() { return displayName; }
}
