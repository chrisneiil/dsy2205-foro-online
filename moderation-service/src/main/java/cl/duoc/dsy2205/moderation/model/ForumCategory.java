package cl.duoc.dsy2205.moderation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FORUM_CATEGORIES")
public class ForumCategory {
    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    protected ForumCategory() {}

    public Long getId() { return id; }
    public String getName() { return name; }
}
