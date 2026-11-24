package cl.duoc.dsy2205.users.service;

import cl.duoc.dsy2205.users.dto.UserDtos.LoginRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.LoginResponse;
import cl.duoc.dsy2205.users.dto.UserDtos.RegisterRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.RoleSummary;
import cl.duoc.dsy2205.users.dto.UserDtos.UpdateRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.UserResponse;
import cl.duoc.dsy2205.users.model.UserAccount;
import cl.duoc.dsy2205.users.repository.UserRepository;
import cl.duoc.dsy2205.users.security.TokenService;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final TokenService tokens;

    public UserService(UserRepository users, PasswordEncoder passwords, TokenService tokens) {
        this.users = users;
        this.passwords = passwords;
        this.tokens = tokens;
    }

    public UserResponse register(RegisterRequest request) {
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        }
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        UserAccount account = new UserAccount(
            username,
            email,
            passwords.encode(request.password()),
            request.displayName()
        );
        return toResponse(users.save(account));
    }

    public LoginResponse login(LoginRequest request) {
        String identifier = request.username().trim();
        UserAccount account = users.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!account.isActive() || !passwords.matches(request.password(), account.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = tokens.issue(account.getUsername(), account.getRole());
        return new LoginResponse(token, "Bearer", tokens.getTtlSeconds(), toResponse(account));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> all() {
        return users.findAll().stream().map(UserService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        return toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        UserAccount account = users.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (!account.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La cuenta está inactiva");
        }
        return toResponse(account);
    }

    public UserResponse update(Long id, UpdateRequest request, String currentUsername, boolean admin) {
        UserAccount account = find(id);
        if (!admin && !account.getUsername().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes modificar tu propia cuenta");
        }
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (!account.getEmail().equalsIgnoreCase(email) && users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        account.setEmail(email);
        account.setDisplayName(request.displayName());
        if (request.password() != null && !request.password().isBlank()) {
            account.setPasswordHash(passwords.encode(request.password()));
        }
        if (request.active() != null) {
            if (!admin) {
                throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un administrador puede cambiar el estado de una cuenta"
                );
            }
            account.setActive(request.active());
        }
        return toResponse(users.save(account));
    }

    public void delete(Long id) {
        users.delete(find(id));
    }

    @Transactional(readOnly = true)
    public List<RoleSummary> roleSummary() {
        return users.summarizeRoles().stream()
            .map(row -> new RoleSummary(
                (String) row[0],
                (Boolean) row[1],
                ((Number) row[2]).longValue()
            ))
            .toList();
    }

    private UserAccount find(Long id) {
        return users.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private static UserResponse toResponse(UserAccount account) {
        return new UserResponse(
            account.getId(),
            account.getUsername(),
            account.getEmail(),
            account.getDisplayName(),
            account.getRole(),
            account.isActive(),
            account.getCreatedAt()
        );
    }
}
