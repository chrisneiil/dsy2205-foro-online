package cl.duoc.dsy2205.users.controller;

import cl.duoc.dsy2205.users.dto.UserDtos.LoginRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.LoginResponse;
import cl.duoc.dsy2205.users.dto.UserDtos.RegisterRequest;
import cl.duoc.dsy2205.users.dto.UserDtos.UserResponse;
import cl.duoc.dsy2205.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    @GetMapping("/me")
    public UserResponse currentUser(Authentication authentication) {
        return service.getByUsername(authentication.getName());
    }
}
