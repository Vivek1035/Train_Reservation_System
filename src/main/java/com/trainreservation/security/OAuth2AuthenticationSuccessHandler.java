package com.trainreservation.security;

import com.trainreservation.entity.User;
import com.trainreservation.enums.UserRole;
import com.trainreservation.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * OAuth2 Authentication Success Handler
 * Handles successful OAuth2 login, creates user if not exists, generates JWT token
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract user info from OAuth2 provider
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String providerId = (String) attributes.get("sub"); // Google's user ID
        
        // Split full name safely
        String resolvedFirstName = "User";
        String resolvedLastName = "";

        if (name != null && !name.isBlank()) {
            String[] parts = name.split(" ", 2);
            resolvedFirstName = parts[0];
            if (parts.length > 1) {
                resolvedLastName = parts[1];
            }
        }

        // Make them final (or effectively final)
        final String firstName = resolvedFirstName;
        final String lastName = resolvedLastName;

        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createOAuth2User(email, firstName, lastName, providerId));
        
        // Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                Collections.singletonList("ROLE_" + user.getRole().name())
        );
        
        // Redirect to frontend with token
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", token)
                .queryParam("email", user.getEmail())
                .queryParam("firstName", user.getFirstName())
                .queryParam("lastName", user.getLastName())
                .build()
                .toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Create new user from OAuth2 data
     */
    private User createOAuth2User(String email, String firstName, String lastName, String providerId) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password("") // No password for OAuth2 users
                .role(UserRole.USER)
                .active(true)
                .oauthProvider("GOOGLE")
                .oauthProviderId(providerId)
                .build();
        
        return userRepository.save(user);
    }
}
