package com.sms.student_management.Controller;

import com.sms.student_management.Entity.User;
import com.sms.student_management.Repository.UserRepository;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = users.findByEmail(request.email().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Invalid demo account or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid demo account or password");
        }

        String requestedRole = request.role().trim().toUpperCase(Locale.ROOT);
        if (!user.getRole().name().equals(requestedRole)) {
            throw new IllegalArgumentException("Select the role associated with this demo account");
        }

        return responseFor(user);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@RequestBody SignupRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setDisplayName(request.name().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(User.Role.valueOf(request.role().trim().toUpperCase(Locale.ROOT)));
        return responseFor(users.save(user));
    }

    private AuthResponse responseFor(User user) {
        AuthUser authUser = new AuthUser(
                user.getId().toString(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRole().name().toLowerCase(Locale.ROOT));
        return new AuthResponse("demo-" + UUID.randomUUID(), authUser);
    }

    public record LoginRequest(String email, String password, String role) {}
    public record SignupRequest(String name, String email, String password, String role) {}
    public record AuthUser(String id, String name, String email, String role) {}
    public record AuthResponse(String token, AuthUser user) {}
}
