package com.biteco.reports.controller;

import com.biteco.reports.dto.LoginRequest;
import com.biteco.reports.model.User;
import com.biteco.reports.repository.UserRepository;
import com.biteco.reports.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.jwtService      = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtService.generateToken(
                user.getUsername(), user.getCompanyId(), user.getRole());

        return ResponseEntity.ok(Map.of(
                "token",     token,
                "username",  user.getUsername(),
                "companyId", user.getCompanyId(),
                "role",      user.getRole()
        ));
    }
}
