package in.tablese.tablese_core.service;

import in.tablese.tablese_core.dto.RegistrationRequest;
import in.tablese.tablese_core.model.Restaurant;
import in.tablese.tablese_core.model.User;
import in.tablese.tablese_core.repository.RestaurantRepository;
import in.tablese.tablese_core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewRestaurant(RegistrationRequest request) {
        // Optional: Check if username is already taken
        if (userRepository.findByUsername(request.ownerName()).isPresent()) {
            // In a real app, throw a custom exception
            throw new IllegalStateException("Username already exists");
        }
        
        // 1. Create and save the new Restaurant
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(request.restaurantName());
        newRestaurant.setAddress(request.restaurantAddress());
        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);

        // 2. Create the new User (the owner)
        User owner = new User();
        owner.setUsername(request.ownerName());
        owner.setPassword(passwordEncoder.encode(request.password()));
        owner.setRole("ADMIN"); // The first user is always an Admin

        // 3. Link the User to the new Restaurant
        owner.setRestaurant(savedRestaurant);
        
        userRepository.save(owner);
    }
}