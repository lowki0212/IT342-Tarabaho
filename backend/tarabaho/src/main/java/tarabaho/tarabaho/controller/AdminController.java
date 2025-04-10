package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.payload.LoginRequest;
import tarabaho.tarabaho.service.AdminService;
import tarabaho.tarabaho.service.UserService;
import tarabaho.tarabaho.service.WorkerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

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
            // Set token in cookie
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
}