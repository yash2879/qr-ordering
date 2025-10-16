package in.tablese.tablese_core.controller;

import in.tablese.tablese_core.dto.LoginRequest;
import in.tablese.tablese_core.dto.LoginResponse;
import in.tablese.tablese_core.dto.RegistrationRequest;
import in.tablese.tablese_core.service.JwtService;
import in.tablese.tablese_core.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Lombok annotation to create a constructor for final fields
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RegistrationService registrationService;


    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@Valid @RequestBody RegistrationRequest request) {
        try {
            registrationService.registerNewRestaurant(request);
            // Return 201 Created on success
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {
            // Return 409 Conflict if username already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        // 1. Authenticate the user
        // This line tells Spring Security to take the username and password, find the user
        // in the database, and compare the hashed passwords.
        // If credentials are bad, it throws an exception which will be caught by our
        // global exception handler and returned as a 401 Unauthorized.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // 2. If authentication is successful, get the UserDetails object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Generate the JWT token
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Return the token in the response
        return ResponseEntity.ok(new LoginResponse(jwtToken));
    }
}