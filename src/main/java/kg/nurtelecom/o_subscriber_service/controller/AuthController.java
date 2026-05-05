package kg.nurtelecom.o_subscriber_service.controller;

import jakarta.validation.Valid;
import kg.nurtelecom.o_subscriber_service.dto.LoginRequest;
import kg.nurtelecom.o_subscriber_service.dto.LoginResponse;
import kg.nurtelecom.o_subscriber_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}