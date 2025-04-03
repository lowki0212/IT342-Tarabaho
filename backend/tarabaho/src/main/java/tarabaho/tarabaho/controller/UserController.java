package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "User Controller", description = "Handles user registration, login, logout, and profile operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Register a new user", description = "Registers a new user after checking for uniqueness")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username, email, or phone already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Username already exists.");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Email already exists.");
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() &&
                userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Phone number already exists.");
        }

        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "Login user", description = "Authenticate user with username and password")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged in successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginData, HttpServletRequest request) {
        try {
            User user = userService.loginUser(loginData.getUsername(), loginData.getPassword());
            request.getSession(true).setAttribute("user", user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Get current logged-in user", description = "Returns the user info of the authenticated Google OAuth user")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/me")
    public User getCurrentUser(@Parameter(hidden = true) @AuthenticationPrincipal OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        Optional<User> user = userService.findByEmail(email);
        return user.orElse(null);
    }

    @Operation(summary = "Update phone number", description = "Updates the phone number for the currently logged-in user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Phone number updated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update-phone")
    public String updatePhone(
        @Parameter(hidden = true) @AuthenticationPrincipal OAuth2User oauthUser,
        @RequestBody PhoneUpdateRequest request
    ) {
        String email = oauthUser.getAttribute("email");
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPhoneNumber(request.getPhoneNumber());
            userService.saveUser(user);
            return "Phone number updated successfully.";
        }

        return "User not found.";
    }

    @Operation(summary = "Logout user", description = "Logs out the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged out successfully"),
        @ApiResponse(responseCode = "500", description = "Logout failed")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
            return ResponseEntity.ok("User logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Logout failed.");
        }
    }

    // DTO for request body
    static class PhoneUpdateRequest {
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
