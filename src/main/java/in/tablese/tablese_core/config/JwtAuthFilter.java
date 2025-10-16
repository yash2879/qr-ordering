package in.tablese.tablese_core.config; // Or your filter package

import in.tablese.tablese_core.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component // Make this a Spring bean
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug("JwtAuthFilter: Processing request URI: {}", request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Check if the Authorization header is present and correctly formatted
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("JwtAuthFilter: Missing or invalid Authorization header: {}", authHeader);
            filterChain.doFilter(request, response); // If not, pass to the next filter
            return;
        }

        // 2. Extract the token from the header
        jwt = authHeader.substring(7); // "Bearer ".length() is 7
        log.debug("JwtAuthFilter: Extracted JWT: {}", jwt);

        // 3. Extract the username from the token using our JwtService
        username = jwtService.extractUsername(jwt);
        log.debug("JwtAuthFilter: Extracted username from JWT: {}", username);

        // 4. Check if the user is not already authenticated for this session
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("JwtAuthFilter: Loading user details for username: {}", username);
            // Load the user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Validate the token against the user details
            boolean tokenValid = jwtService.isTokenValid(jwt, userDetails);
            log.debug("JwtAuthFilter: Token valid for user {}: {}", username, tokenValid);
            if (tokenValid) {
                // 6. If valid, create an authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't have credentials in a JWT context
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("JwtAuthFilter: Authentication set for user: {}", username);
            }
        } else {
            log.debug("JwtAuthFilter: Skipping authentication. Username: {}, Already Authenticated: {}", username, SecurityContextHolder.getContext().getAuthentication() != null);
        }
        
        // 7. Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}