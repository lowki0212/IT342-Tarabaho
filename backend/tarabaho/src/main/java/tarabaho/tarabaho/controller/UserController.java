package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import tarabaho.tarabaho.dto.AuthResponse;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "User Controller", description = "Handles user registration, login, logout, and profile operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @Operation(
        summary = "Generate JWT token",
        description = "Authenticate user with username and password and return JWT token as JSON"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful, token returned"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> generateToken(
            @RequestBody LoginRequest loginData,
            HttpServletResponse response
    ) {
        try {
            User user = userService.loginUser(loginData.getUsername(), loginData.getPassword());
            String jwtToken = jwtUtil.generateToken(user.getUsername());

            Cookie tokenCookie = new Cookie("jwtToken", jwtToken);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(false);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(24 * 60 * 60);
            tokenCookie.setDomain("localhost");
            response.addCookie(tokenCookie);

            AuthResponse body = new AuthResponse(jwtToken);
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Login user (session-based)", description = "Authenticate user with username and password and store in session")
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

    @Operation(summary = "Get current logged-in user", description = "Returns the user info of the authenticated user")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        return user.orElse(null);
    }

    @Operation(summary = "Update phone number", description = "Updates the phone number for the currently logged-in user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Phone number updated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update-phone")
    public String updatePhone(
            Authentication authentication,
            @RequestBody PhoneUpdateRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "User not authenticated.";
        }
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPhoneNumber(request.getPhoneNumber());
            userService.saveUser(user);
            return "Phone number updated successfully.";
        }

        return "User not found.";
    }

    @Operation(summary = "Upload profile picture", description = "Uploads a profile picture for the currently logged-in user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "No file uploaded or invalid file"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Failed to upload file")
    })
    @PostMapping("/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }

            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            String uploadDir = "uploads/profiles/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to create upload directory: " + uploadDir);
                }
            }

            if (!directory.canWrite()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Upload directory is not writable: " + uploadDir);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();
            System.out.println("Saving file to: " + filePath.toString());

            Files.write(filePath, file.getBytes());

            User user = userOpt.get();
            user.setProfilePicture("/profiles/" + fileName);
            userService.saveUser(user);

            return ResponseEntity.ok(user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Update user profile", description = "Updates email, address, birthday, and password for the currently logged-in user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (userService.findByEmail(request.getEmail()).isPresent() && !request.getEmail().equals(user.getEmail())) {
                return ResponseEntity.badRequest().body("⚠️ Email already exists.");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getBirthday() != null) {
            try {
                LocalDate birthday = LocalDate.parse(request.getBirthday());
                user.setBirthday(birthday);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid birthday format. Use YYYY-MM-DD.");
            }
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }

        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Logout user", description = "Logs out the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged out successfully"),
        @ApiResponse(responseCode = "500", description = "Logout failed")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Entering /logout endpoint");
            request.logout();
            System.out.println("After request.logout()");

            Cookie tokenCookie = new Cookie("jwtToken", null);
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath("/");
            tokenCookie.setDomain("localhost");
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(false);
            response.addCookie(tokenCookie);
            System.out.println("Cookie added to response: jwtToken=; Path=/; Domain=localhost; Max-Age=0; HttpOnly");

            return ResponseEntity.ok("User logged out successfully.");
        } catch (Exception e) {
            System.err.println("Logout failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Logout failed: " + e.getMessage());
        }
    }

    static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class PhoneUpdateRequest {
        private String phoneNumber;

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }

    static class ProfileUpdateRequest {
        private String email;
        private String location;
        private String birthday;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getBirthday() { return birthday; }
        public void setBirthday(String birthday) { this.birthday = birthday; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}