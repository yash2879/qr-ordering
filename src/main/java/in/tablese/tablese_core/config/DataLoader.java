package in.tablese.tablese_core.config;

import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.model.User;
import in.tablese.tablese_core.repository.RestaurantRepository;
import in.tablese.tablese_core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only run if there are no restaurants
        if (restaurantRepository.count() == 0) {
            System.out.println("No data found. Seeding database...");

            // 1. Create a default restaurant
            Restaurant defaultRestaurant = new Restaurant();
            defaultRestaurant.setName("My First Restaurant");
            defaultRestaurant.setAddress("123 Main St");
            Restaurant savedRestaurant = restaurantRepository.save(defaultRestaurant);
            System.out.println("Created default restaurant with ID: " + savedRestaurant.getId());

            // 2. Create a default admin user and link it to the restaurant
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setRole("ADMIN");
            adminUser.setRestaurant(savedRestaurant); // Link the user

            userRepository.save(adminUser);
            System.out.println("Default admin user created and linked to restaurant.");
        }
    }
}