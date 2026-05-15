package com.biteco.reports.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.biteco.reports.model.User;
import com.biteco.reports.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearUsuario("carlos.retail", "Retail2025!", "retail-colombia-001", "ROLE_CLIENT");
        crearUsuario("ana.fintech", "Fintech2025!", "fintech-bogota-002", "ROLE_CLIENT");
        crearUsuario("admin.biteco", "Admin2025!", "biteco-admin", "ROLE_ADMIN");
        System.out.println("\n✅ Usuarios creados correctamente");
    }

    private void crearUsuario(String username, String password, String companyId, String role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setCompanyId(companyId);
            u.setRole(role);
            userRepository.save(u);
            System.out.println("  Creado: " + username);
        }
    }
}
