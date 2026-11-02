package cl.duoc.dsy2205.users.service;

import cl.duoc.dsy2205.users.dto.UserDtos.*;
import cl.duoc.dsy2205.users.model.UserAccount;
import cl.duoc.dsy2205.users.repository.UserRepository;
import cl.duoc.dsy2205.users.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final TokenService tokens;
    public UserService(UserRepository users, PasswordEncoder passwords, TokenService tokens) {
        this.users=users; this.passwords=passwords; this.tokens=tokens;
    }
    public UserResponse register(RegisterRequest request) {
        if (users.existsByUsername(request.username())) throw new ResponseStatusException(HttpStatus.CONFLICT,"El nombre de usuario ya existe");
        if (users.existsByEmail(request.email())) throw new ResponseStatusException(HttpStatus.CONFLICT,"El correo ya está registrado");
        return response(users.save(new UserAccount(request.username(),request.email(),passwords.encode(request.password()),request.displayName())));
    }
    public LoginResponse login(LoginRequest request) {
        UserAccount user=users.findByUsername(request.username()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas"));
        if (!user.isActive() || !passwords.matches(request.password(),user.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
        return new LoginResponse(tokens.issue(user.getUsername(),user.getRole()),"Bearer",tokens.getTtlSeconds(),response(user));
    }
    public List<UserResponse> all() { return users.findAll().stream().map(UserService::response).toList(); }
    public UserResponse get(Long id) { return response(find(id)); }
    public UserResponse update(Long id, UpdateRequest request, String currentUser, boolean admin) {
        UserAccount user=find(id);
        if (!admin && !user.getUsername().equals(currentUser)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo puedes modificar tu propia cuenta");
        if (!user.getEmail().equalsIgnoreCase(request.email()) && users.existsByEmail(request.email())) throw new ResponseStatusException(HttpStatus.CONFLICT,"El correo ya está registrado");
        user.setEmail(request.email()); user.setDisplayName(request.displayName());
        if (request.password()!=null && !request.password().isBlank()) user.setPasswordHash(passwords.encode(request.password()));
        if (request.active()!=null) {
            if (!admin) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo un administrador puede cambiar el estado de una cuenta");
            user.setActive(request.active());
        }
        return response(users.save(user));
    }
    public void delete(Long id) { users.delete(find(id)); }
    public List<RoleSummary> roleSummary() {
        return users.summarizeRoles().stream().map(row -> new RoleSummary((String)row[0], (Boolean)row[1], ((Number)row[2]).longValue())).toList();
    }
    private UserAccount find(Long id) { return users.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado")); }
    public static UserResponse response(UserAccount u) { return new UserResponse(u.getId(),u.getUsername(),u.getEmail(),u.getDisplayName(),u.getRole(),u.isActive(),u.getCreatedAt()); }
}
