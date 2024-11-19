package org.sid.sgbankbackend.security;

import org.sid.sgbankbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Security controller responsible for handling authentication requests.
 * This controller provides endpoints for user login and generates JWT tokens.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")// Base URL for authentication-related endpoints
public class SecurityController {

    @Autowired
    private AuthenticationProvider authenticationProvider; // Handles authentication logic

    @Autowired
    private JwtService jwtService; // Service for generating JWT tokens

    /**
     * Authenticates the user and generates a JWT token.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A map containing the generated JWT token.
     */
    @PostMapping("/login")
    public Map<String, String> login(String username, String password) {
        // Create an authentication token with the provided username and password
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        // Generate and return a JWT token for the authenticated user
        return jwtService.generateToken(username, authentication);
    }

}
