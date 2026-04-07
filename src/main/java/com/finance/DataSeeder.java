package com.finance;

import com.finance.model.Role;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        // Only seed if no users exist
        if (userRepository.count() == 0) {

            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail("admin@finance.com");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            System.out.println(">>> Seeded default ADMIN: admin@finance.com");
        }
    }
}