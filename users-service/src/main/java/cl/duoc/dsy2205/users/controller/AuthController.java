package cl.duoc.dsy2205.users.controller;

import cl.duoc.dsy2205.users.dto.UserDtos.*;
import cl.duoc.dsy2205.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;
    public AuthController(UserService service) { this.service=service; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) { return service.register(request); }
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest request) { return service.login(request); }
}
