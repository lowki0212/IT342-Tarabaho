package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@RestController
@Tag(name = "OAuth2 Login", description = "Handles successful login and auto-registration with Google OAuth2")
public class OAuth2Controller {

    @Autowired
    private UserRepository userRepository;

    @Operation(
        summary = "Handle Google OAuth2 login success",
        description = "Registers a new user if not yet in the database, then redirects to user home"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirects to frontend (home or failure)"),
        @ApiResponse(responseCode = "500", description = "Server error during processing")
    })
    @GetMapping("/oauth2-success")
    @Transactional
    public void oauth2Success(
        @Parameter(hidden = true)
        @AuthenticationPrincipal OAuth2User oauthUser,
        HttpServletResponse response
    ) throws IOException {
        if (oauthUser == null) {
            System.out.println("⚠️ OAuth2User is null");
            response.sendRedirect("http://localhost:5173/login-failed");
            return;
        }

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        System.out.println("🔍 Logged-in email from Google: " + email);

        Optional<User> existingUser = userRepository.findByEmail(email);
        System.out.println("OAuth user email: " + email);
        System.out.println("Does user exist in DB? " + existingUser.isPresent());
        if (existingUser.isEmpty()) {
            System.out.println("🆕 Registering new user: " + email);
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setUsername(email); // Use email as username
            newUser.setPassword("");    // No password for OAuth
            newUser.setPhoneNumber("");
            newUser.setBirthday(null);

            userRepository.save(newUser);
        } else {
            System.out.println("✅ Existing user found: " + email);
        }

        response.sendRedirect("http://localhost:5173/user/home");
    }

}
