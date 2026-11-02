package cl.duoc.dsy2205.users.controller;

import cl.duoc.dsy2205.users.dto.UserDtos.*;
import cl.duoc.dsy2205.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service=service; }
    @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<UserResponse> all() { return service.all(); }
    @GetMapping("/report/roles") @PreAuthorize("hasRole('ADMIN')") public List<RoleSummary> roleSummary() { return service.roleSummary(); }
    @GetMapping("/{id}") public UserResponse get(@PathVariable Long id, Authentication auth) {
        UserResponse user=service.get(id);
        if (!auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))&&!user.username().equals(auth.getName()))
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Solo puedes consultar tu propia cuenta");
        return user;
    }
    @PutMapping("/{id}") public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request, Authentication auth) {
        return service.update(id,request,auth.getName(),auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")));
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
