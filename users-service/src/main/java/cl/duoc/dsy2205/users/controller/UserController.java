package cl.duoc.dsy2205.users.controller;

import cl.duoc.dsy2205.users.dto.UserDtos.RoleSummary;
import cl.duoc.dsy2205.users.dto.UserDtos.UpdateRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.UserResponse;
import cl.duoc.dsy2205.users.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> all() {
        return service.all();
    }

    @GetMapping("/report/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleSummary> roleSummary() {
        return service.roleSummary();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id, Authentication authentication) {
        UserResponse user = service.get(id);
        boolean isAdmin = hasAdminRole(authentication);
        if (!isAdmin && !user.username().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes consultar tu propia cuenta");
        }
        return user;
    }

    @PutMapping("/{id}")
    public UserResponse update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateRequest request,
        Authentication authentication
    ) {
        return service.update(id, request, authentication.getName(), hasAdminRole(authentication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
