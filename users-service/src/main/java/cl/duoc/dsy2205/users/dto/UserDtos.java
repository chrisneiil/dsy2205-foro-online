package cl.duoc.dsy2205.users.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public final class UserDtos {
    private UserDtos() {}
    public record RegisterRequest(@NotBlank @Size(min=3,max=40) String username,
        @NotBlank @Email @Size(max=120) String email,
        @NotBlank @Size(min=8,max=72) String password,
        @NotBlank @Size(max=80) String displayName) {}
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record UpdateRequest(@NotBlank @Email @Size(max=120) String email,
        @NotBlank @Size(max=80) String displayName, @Size(min=8,max=72) String password, Boolean active) {}
    public record UserResponse(Long id, String username, String email, String displayName,
        String role, boolean active, LocalDateTime createdAt) {}
    public record LoginResponse(String token, String tokenType, long expiresInSeconds, UserResponse user) {}
    public record RoleSummary(String role, boolean active, long total) {}
}
