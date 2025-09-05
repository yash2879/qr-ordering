package in.tablese.tablese_core.config;

import in.tablese.tablese_core.model.User;
import in.tablese.tablese_core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if any users exist
        if (userRepository.count() == 0) {
            System.out.println("No users found. Creating default admin user...");
            
            User adminUser = new User();
            adminUser.setUsername("admin");
            // IMPORTANT: Always store hashed passwords
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setRole("ADMIN");
            
            userRepository.save(adminUser);
            
            System.out.println("Default admin user created.");
        }
    }
}