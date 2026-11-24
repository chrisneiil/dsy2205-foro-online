package cl.duoc.dsy2205.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class UserDtos {
    private static final String SECURE_PASSWORD = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{10,64}";
    private static final String OPTIONAL_SECURE_PASSWORD = "^$|" + SECURE_PASSWORD;

    private UserDtos() {}

    public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 40) @Pattern(regexp = "[A-Za-z0-9._-]+") String username,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(min = 10, max = 64) @Pattern(regexp = SECURE_PASSWORD) String password,
        @NotBlank @Size(min = 2, max = 80) String displayName
    ) {}

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    public record UpdateRequest(
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(min = 2, max = 80) String displayName,
        @Size(max = 64) @Pattern(regexp = OPTIONAL_SECURE_PASSWORD) String password,
        Boolean active
    ) {}

    public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String role,
        boolean active,
        LocalDateTime createdAt
    ) {}

    public record LoginResponse(String token, String tokenType, long expiresInSeconds, UserResponse user) {}

    public record RoleSummary(String role, boolean active, long total) {}
}
