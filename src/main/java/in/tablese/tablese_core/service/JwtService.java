package in.tablese.tablese_core.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import in.tablese.tablese_core.security.CustomUserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 1. Secret Key: A strong, private key used to sign the tokens.
    //    It's crucial this is kept secret. We'll define it in application.properties.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // --- Core Public Methods ---

    /**
     * Extracts the username from a JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token for a given user without extra claims.
     */
    public String generateToken(UserDetails userDetails) {
        // Create a map to hold our custom claims
        Map<String, Object> claims = new HashMap<>();

        // Check if the userDetails is an instance of our CustomUserDetails
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            // If it is, add the restaurantId and roles to the claims map.
            claims.put("restaurantId", customUserDetails.getRestaurantId());
            // We can also add roles if needed, though subject is usually enough
            claims.put("roles", customUserDetails.getAuthorities());
        }

        // Pass the new claims map to the overloaded generateToken method
        return generateToken(claims, userDetails);
    }

    /**
     * Generates a JWT token with extra claims.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    
    /**
     * Validates if a token is correct and not expired.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // --- Helper Methods ---

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // The "subject" of the token is the user
                .setIssuedAt(new Date(System.currentTimeMillis())) // Timestamp of when it was created
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // When it expires
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign it with our secret key
                .compact(); // Build the final, compact URL-safe string
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * A generic method to extract any single piece of information (a "claim") from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the signing key used to sign and verify tokens.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}