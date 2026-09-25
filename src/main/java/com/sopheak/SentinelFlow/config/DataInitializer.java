package com.sopheak.SentinelFlow.config;

import com.sopheak.SentinelFlow.entity.Role;
import com.sopheak.SentinelFlow.entity.User;
import com.sopheak.SentinelFlow.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.email}")
    private String adminEmail;

    @Value("${app.bootstrap.admin.password}")
    private String adminPassword;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail(adminEmail)) {

            System.out.println(
                    "SUPER_ADMIN bootstrap account already exists: "
                            + adminEmail
            );

            return;
        }

        User superAdmin = new User(
                "System Super Admin",
                adminEmail,
                passwordEncoder.encode(adminPassword),
                Role.SUPER_ADMIN
        );

        userRepository.save(superAdmin);

        System.out.println(
                "SUPER_ADMIN bootstrap account created: "
                        + adminEmail
        );
    }
}