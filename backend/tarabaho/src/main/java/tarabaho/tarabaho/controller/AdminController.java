package tarabaho.tarabaho.controller;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tarabaho.tarabaho.dto.WorkerUpdateDTO;
import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.entity.Certificate;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.payload.LoginRequest;
import tarabaho.tarabaho.service.AdminService;
import tarabaho.tarabaho.service.UserService;
import tarabaho.tarabaho.service.WorkerService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Admin Management", description = "Endpoints for managing admin accounts, workers, users, and certificates")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Get all admins", description = "Retrieve a list of all admin accounts")
    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all user accounts")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user account by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Admin not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<User> user = adminService.findUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Get all workers", description = "Retrieve a list of all worker accounts")
    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @Operation(summary = "Register a new admin", description = "Create a new admin account")
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            Admin registered = adminService.registerAdmin(admin);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Register a new user", description = "Create a new user account")
    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registered = userService.registerUser(user);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Register a new worker", description = "Create a new worker account")
    @PostMapping("/workers/register")
    public ResponseEntity<?> registerWorker(@RequestBody Worker worker) {
        try {
            Worker registered = workerService.registerWorker(worker);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Admin login", description = "Authenticate an admin and return a JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Admin admin = adminService.loginAdmin(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(admin.getUsername());
            Cookie tokenCookie = new Cookie("jwtToken", token);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(false); // Set to true in production
            tokenCookie.setPath("/");
            tokenCookie.setDomain("localhost");
            tokenCookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(tokenCookie);
            return ResponseEntity.ok("Admin login successful");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @Operation(summary = "Delete an admin", description = "Delete an admin account by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin not found or cannot be deleted");
        }
    }

    @Operation(summary = "Edit an admin", description = "Update an admin account by ID")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        try {
            Admin admin = adminService.editAdmin(id, updatedAdmin);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a user", description = "Delete a user account by ID")
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found or cannot be deleted");
        }
    }

    @Operation(summary = "Edit a user", description = "Update a user account by ID")
    @PutMapping("/users/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.editUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a worker", description = "Delete a worker account by ID")
    @DeleteMapping("/workers/delete/{id}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long id, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not authenticated.");
            }
            workerService.deleteWorker(id);
            return ResponseEntity.ok("Worker deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Worker not found or cannot be deleted: " + e.getMessage());
        }
    }

    @Operation(summary = "Edit a worker", description = "Update a worker account by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Worker updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Admin not authenticated"),
        @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @PutMapping("/workers/edit/{id}")
    public ResponseEntity<?> editWorker(
            @PathVariable Long id,
            @RequestBody WorkerUpdateDTO workerDTO,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not authenticated.");
            }
            Worker worker = adminService.editWorker(id, workerDTO);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update worker: " + e.getMessage());
        }
    }

    @Operation(summary = "Add categories to a worker", description = "Add one or more categories to a worker by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category IDs or worker not found"),
        @ApiResponse(responseCode = "401", description = "Admin not authenticated")
    })
    @PostMapping("/workers/{id}/categories")
    public ResponseEntity<?> addCategoriesToWorker(
            @PathVariable Long id,
            @RequestBody List<Long> categoryIds,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not authenticated.");
            }
            Worker worker = adminService.addCategoriesToWorker(id, categoryIds);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add categories: " + e.getMessage());
        }
    }

    @Operation(summary = "Get certificates for a worker", description = "Retrieve all certificates associated with a worker")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of certificates returned successfully"),
        @ApiResponse(responseCode = "401", description = "Admin not authenticated"),
        @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @GetMapping("/certificates/worker/{workerId}")
    public ResponseEntity<?> getCertificatesByWorkerId(
            @PathVariable Long workerId,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not authenticated.");
            }
            List<Certificate> certificates = adminService.getCertificatesByWorkerId(workerId);
            return ResponseEntity.ok(certificates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch certificates: " + e.getMessage());
        }
    }

    @Operation(summary = "Admin logout", description = "Log out an admin by clearing the JWT token")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    @Operation(summary = "Get current admin", description = "Retrieve the currently authenticated admin")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAdmin(HttpServletRequest request) {
        try {
            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
            }
            String username = jwtUtil.getUsernameFromToken(token);
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @Operation(summary = "Upload admin profile picture", description = "Upload a profile picture for the authenticated admin")
    @PostMapping("/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
            }
            String username = jwtUtil.getUsernameFromToken(token);
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + File.separator + "uploads" + File.separator + "profiles" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File serverFile = new File(uploadDir + fileName);
            file.transferTo(serverFile);

            String filePath = "/profiles/" + fileName;
            Admin updatedAdmin = adminService.updateProfilePicture(admin.getId(), filePath);
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload picture: " + e.getMessage());
        }
    }
}