package tarabaho.tarabaho.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.payload.LoginRequest;
import tarabaho.tarabaho.service.AdminService;
import tarabaho.tarabaho.service.UserService;
import tarabaho.tarabaho.service.WorkerService;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Endpoints for managing admin accounts and dashboard data")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            Admin registered = adminService.registerAdmin(admin);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registered = userService.registerUser(user);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/workers/register")
    public ResponseEntity<?> registerWorker(@RequestBody Worker worker) {
        try {
            Worker registered = workerService.registerWorker(worker);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin not found or cannot be deleted");
        }
    }
    
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        try {
            Admin admin = adminService.editAdmin(id, updatedAdmin);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found or cannot be deleted");
        }
    }

    @PutMapping("/users/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.editUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/workers/delete/{id}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long id) {
        try {
            workerService.deleteWorker(id);
            return ResponseEntity.ok("Worker deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Worker not found or cannot be deleted");
        }
    }

    @PutMapping("/workers/edit/{id}")
    public ResponseEntity<?> editWorker(@PathVariable Long id, @RequestBody Worker updatedWorker) {
        try {
            Worker worker = workerService.editWorker(id, updatedWorker);
            return ResponseEntity.ok(worker);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAdmin(HttpServletRequest request) {
        try {
            String token = null;
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (token == null) {
                return ResponseEntity.status(401).body("No token found");
            }
            String username = jwtUtil.getUsernameFromToken(token); // Updated method name
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin not found");
            }
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @PostMapping("/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            String token = null;
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
            if (token == null) {
                return ResponseEntity.status(401).body("No token found");
            }
            String username = jwtUtil.getUsernameFromToken(token);
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin not found");
            }
    
            // Use absolute path based on project root
            String projectRoot = System.getProperty("user.dir"); // Gets the project root directory
            String uploadDir = projectRoot + File.separator + "uploads" + File.separator + "profiles" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File serverFile = new File(uploadDir + fileName);
            file.transferTo(serverFile);
    
            // Update admin's profile picture path
            String filePath = "/profiles/" + fileName;
            Admin updatedAdmin = adminService.updateProfilePicture(admin.getId(), filePath);
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload picture: " + e.getMessage());
        }
    }
}