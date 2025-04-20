package tarabaho.tarabaho.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import jakarta.servlet.http.HttpServletResponse;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.service.WorkerService;

@RestController
@RequestMapping("/api/worker")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Worker Controller", description = "Handles registration, login, and management of workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Check for duplicate worker details", description = "Checks if username, email, or phone number already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "No duplicates found"),
        @ApiResponse(responseCode = "400", description = "Username, email, or phone number already exists")
    })
    @PostMapping("/check-duplicates")
    public ResponseEntity<?> checkDuplicates(@RequestBody Worker worker) {
        System.out.println("WorkerController: Checking duplicates for username: " + worker.getUsername());
        
        if (workerService.findByUsername(worker.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Username already exists.");
        }
        if (workerService.findByEmail(worker.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Email already exists.");
        }
        if (worker.getPhoneNumber() != null && !worker.getPhoneNumber().isEmpty() &&
                workerService.findByPhoneNumber(worker.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Phone number already exists.");
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Register new worker", description = "Registers a new worker in the system after checking for uniqueness")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Worker registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username, email, or phone already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerWorker(@RequestBody Worker worker, HttpServletResponse response) {
        System.out.println("WorkerController: Received registration request for username: " + worker.getUsername());
        
        if (workerService.findByUsername(worker.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Username already exists.");
        }
        if (workerService.findByEmail(worker.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Email already exists.");
        }
        if (worker.getPhoneNumber() != null && !worker.getPhoneNumber().isEmpty() &&
                workerService.findByPhoneNumber(worker.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Phone number already exists.");
        }

        Worker registeredWorker = workerService.registerWorker(worker);
        System.out.println("WorkerController: Worker registered successfully, ID: " + registeredWorker.getId());
        return ResponseEntity.ok(registeredWorker);
    }

    @Operation(summary = "Upload initial profile picture during registration", description = "Allows uploading a 2x2 profile picture for a newly registered worker without authentication, only if no picture exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "No file uploaded, invalid file, or profile picture already exists"),
        @ApiResponse(responseCode = "404", description = "Worker not found"),
        @ApiResponse(responseCode = "500", description = "Failed to upload file")
    })
    @PostMapping("/{workerId}/upload-initial-picture")
    public ResponseEntity<?> uploadInitialProfilePicture(
            @PathVariable Long workerId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            System.out.println("WorkerController: Starting upload-initial-picture for workerId: " + workerId);

            // Validate worker
            Worker worker = workerService.findById(workerId);
            if (worker == null) {
                System.out.println("WorkerController: Worker not found for ID: " + workerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found.");
            }

            // Check existing profile picture
            if (worker.getProfilePicture() != null && !worker.getProfilePicture().isEmpty()) {
                System.out.println("WorkerController: Profile picture already exists for workerId: " + workerId);
                return ResponseEntity.badRequest().body("Profile picture already exists.");
            }

            // Validate file
            if (file == null || file.isEmpty()) {
                System.out.println("WorkerController: No file uploaded for workerId: " + workerId);
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            // Check file size (max 5MB)
            long maxFileSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxFileSize) {
                System.out.println("WorkerController: File size exceeds limit for workerId: " + workerId + ", size: " + file.getSize());
                return ResponseEntity.badRequest().body("File size exceeds 5MB limit.");
            }

            // Validate content type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                System.out.println("WorkerController: Invalid file type for workerId: " + workerId + ", contentType: " + contentType);
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }

            // Set up upload directory
            String uploadDir = "uploads/profiles/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                System.out.println("WorkerController: Creating upload directory: " + uploadDir);
                boolean created = directory.mkdirs();
                if (!created) {
                    System.out.println("WorkerController: Failed to create upload directory: " + uploadDir);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to create upload directory: " + uploadDir);
                }
            }

            // Check directory permissions
            if (!directory.canWrite()) {
                System.out.println("WorkerController: Upload directory is not writable: " + uploadDir);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Upload directory is not writable: " + uploadDir);
            }

            // Generate file name and path
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                System.out.println("WorkerController: Invalid original filename for workerId: " + workerId);
                return ResponseEntity.badRequest().body("Invalid file name.");
            }
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();
            System.out.println("WorkerController: Saving file to: " + filePath);

            // Save file
            Files.write(filePath, file.getBytes());
            System.out.println("WorkerController: File saved successfully: " + filePath);

            // Update worker
            String profilePicturePath = "/profiles/" + fileName;
            worker.setProfilePicture(profilePicturePath);
            workerService.editWorker(workerId, worker);
            System.out.println("WorkerController: Worker updated with profile picture for workerId: " + workerId);

            System.out.println("WorkerController: Initial profile picture uploaded successfully for workerId: " + workerId);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            e.printStackTrace(); // Log full stack trace
            System.out.println("WorkerController: Initial profile picture upload failed for workerId: " + workerId + ", error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @Operation(summary = "Generate JWT token for worker", description = "Authenticate worker with username and password and return JWT token as JSON")
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
            System.out.println("WorkerController: Attempting login for username: " + loginData.getUsername());
            Worker worker = workerService.loginWorker(loginData.getUsername(), loginData.getPassword());
            String jwtToken = jwtUtil.generateToken(worker.getUsername());

            Cookie tokenCookie = new Cookie("jwtToken", jwtToken);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(false);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(24 * 60 * 60);
            tokenCookie.setDomain("localhost");
            response.addCookie(tokenCookie);
            System.out.println("WorkerController: Token generated and cookie set for username: " + worker.getUsername());

            AuthResponse body = new AuthResponse(jwtToken);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            System.out.println("WorkerController: Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, e.getMessage()));
        }
    }

    @Operation(summary = "Login worker (session-based)", description = "Authenticate a worker using username and password")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Worker logged in successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginWorker(@RequestBody Worker worker) {
        try {
            System.out.println("WorkerController: Attempting session login for username: " + worker.getUsername());
            Worker loggedInWorker = workerService.loginWorker(worker.getUsername(), worker.getPassword());
            return ResponseEntity.ok(loggedInWorker);
        } catch (Exception e) {
            System.out.println("WorkerController: Session login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @Operation(summary = "Upload profile picture", description = "Uploads a profile picture for a worker after authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "No file uploaded or invalid file"),
        @ApiResponse(responseCode = "401", description = "Worker not authenticated"),
        @ApiResponse(responseCode = "403", description = "Unauthorized to upload picture for another worker"),
        @ApiResponse(responseCode = "404", description = "Worker not found"),
        @ApiResponse(responseCode = "500", description = "Failed to upload file")
    })
    @PostMapping("/{workerId}/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long workerId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Worker not authenticated.");
            }

            String username = authentication.getName();
            Worker worker = workerService.findByUsername(username)
                    .orElseThrow(() -> new Exception("Worker not found for username: " + username));

            if (!worker.getId().equals(workerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Unauthorized: Cannot upload picture for another worker");
            }

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }

            String uploadDir = "Uploads/profiles/";
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
            Files.write(filePath, file.getBytes());

            String profilePicturePath = "/profiles/" + fileName;
            worker.setProfilePicture(profilePicturePath);
            workerService.editWorker(workerId, worker);

            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            System.out.println("WorkerController: Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all workers", description = "Retrieve a list of all registered workers")
    @ApiResponse(responseCode = "200", description = "List of workers returned successfully")
    @GetMapping("/all")
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @Operation(summary = "Delete worker", description = "Deletes a worker by ID")
    @ApiResponse(responseCode = "200", description = "Worker deleted successfully")
    @DeleteMapping("/{id}")
    public void deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
    }

    @Operation(summary = "Update worker profile", description = "Updates profile details for the authenticated worker")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or unauthorized"),
        @ApiResponse(responseCode = "401", description = "Worker not authenticated"),
        @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorker(
            @PathVariable Long id,
            @RequestBody Worker updatedWorker,
            Authentication authentication
    ) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Worker not authenticated.");
            }

            String username = authentication.getName();
            Worker existingWorker = workerService.findByUsername(username)
                    .orElseThrow(() -> new Exception("Worker not found for username: " + username));

            if (!existingWorker.getId().equals(id)) {
                throw new Exception("Unauthorized: Cannot update another worker's profile");
            }

            if (updatedWorker.getEmail() != null && !updatedWorker.getEmail().isEmpty()) {
                if (workerService.findByEmail(updatedWorker.getEmail()).isPresent() &&
                    !updatedWorker.getEmail().equals(existingWorker.getEmail())) {
                    return ResponseEntity.badRequest().body("⚠️ Email already exists.");
                }
                existingWorker.setEmail(updatedWorker.getEmail());
            }
            if (updatedWorker.getAddress() != null) {
                existingWorker.setAddress(updatedWorker.getAddress());
            }
            if (updatedWorker.getBiography() != null) {
                existingWorker.setBiography(updatedWorker.getBiography());
            }
            if (updatedWorker.getBirthday() != null) {
                existingWorker.setBirthday(updatedWorker.getBirthday());
            }
            if (updatedWorker.getPassword() != null && !updatedWorker.getPassword().isEmpty()) {
                existingWorker.setPassword(updatedWorker.getPassword());
            }
            if (updatedWorker.getPhoneNumber() != null) {
                if (workerService.findByPhoneNumber(updatedWorker.getPhoneNumber()).isPresent() &&
                    !updatedWorker.getPhoneNumber().equals(existingWorker.getPhoneNumber())) {
                    return ResponseEntity.badRequest().body("⚠️ Phone number already exists.");
                }
                existingWorker.setPhoneNumber(updatedWorker.getPhoneNumber());
            }
            if (updatedWorker.getFirstName() != null) {
                existingWorker.setFirstName(updatedWorker.getFirstName());
            }
            if (updatedWorker.getLastName() != null) {
                existingWorker.setLastName(updatedWorker.getLastName());
            }

            Worker updated = workerService.editWorker(id, existingWorker);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.out.println("WorkerController: Update failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update profile: " + e.getMessage());
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

    static class AuthResponse {
        private String token;
        private String error;

        public AuthResponse(String token) {
            this.token = token;
        }

        public AuthResponse(String token, String error) {
            this.token = token;
            this.error = error;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}