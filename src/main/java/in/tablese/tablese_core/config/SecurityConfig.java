package in.tablese.tablese_core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- CONSOLIDATED SINGLE SECURITY FILTER CHAIN ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS for all requests
                .cors(Customizer.withDefaults())

                // Disable CSRF ONLY for our API paths, but keep it enabled for the web UI
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                // Define authorization rules for all requests
                .authorizeHttpRequests(auth -> auth
                        // Public Static Resources
                        .requestMatchers("/css/**", "/vendor/**", "/js/**", "/scss/**", "/img/**").permitAll()
                        // Public API Endpoints
                        .requestMatchers("/api/health", "/api/debug/**", "/api/menu/**").permitAll()
                        // NEW RULE: Make the customer menu pages public
                        .requestMatchers("/menu/**").permitAll()
                        // The login and registration pages must be public
                        .requestMatchers("/", "/login", "/register").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )

                // Configure form login for our stateful web UI
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", false) // Use intelligent redirect
                        .permitAll()
                )

                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Enable HTTP Basic Auth as another option, which our API can use
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // --- CORS Configuration Bean ---
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}