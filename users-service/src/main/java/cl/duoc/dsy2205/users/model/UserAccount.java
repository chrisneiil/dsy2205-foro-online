package cl.duoc.dsy2205.users.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FORUM_USERS")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 40) private String username;
    @Column(nullable = false, unique = true, length = 120) private String email;
    @Column(name = "PASSWORD_HASH", nullable = false, length = 100) private String passwordHash;
    @Column(name = "DISPLAY_NAME", nullable = false, length = 80) private String displayName;
    @Column(name = "USER_ROLE", nullable = false, length = 12) private String role = "USER";
    @Column(nullable = false) private boolean active = true;
    @Column(name = "CREATED_AT", nullable = false) private LocalDateTime createdAt = LocalDateTime.now();

    protected UserAccount() {}
    public UserAccount(String username, String email, String passwordHash, String displayName) {
        this.username = username; this.email = email; this.passwordHash = passwordHash; this.displayName = displayName;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getDisplayName() { return displayName; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setEmail(String email) { this.email = email; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setActive(boolean active) { this.active = active; }
}
