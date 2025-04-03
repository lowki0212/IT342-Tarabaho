package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.payload.LoginRequest;
import tarabaho.tarabaho.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Endpoints for managing admin accounts")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get all admins", description = "Returns a list of all registered admins")
    @ApiResponse(responseCode = "200", description = "List of admins returned")
    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @Operation(summary = "Register a new admin", description = "Creates a new admin account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin registered successfully", content = @Content(schema = @Schema(implementation = Admin.class))),
        @ApiResponse(responseCode = "400", description = "Invalid admin input", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(
        @RequestBody(description = "Admin details", required = true, content = @Content(schema = @Schema(implementation = Admin.class)))
        @org.springframework.web.bind.annotation.RequestBody Admin admin) {
        try {
            Admin registered = adminService.registerAdmin(admin);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Admin login", description = "Authenticate an admin using username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin logged in successfully", content = @Content(schema = @Schema(implementation = Admin.class))),
        @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(
        @RequestBody(description = "Login credentials", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class)))
        @org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest) {
        try {
            Admin loggedIn = adminService.loginAdmin(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );
            return ResponseEntity.ok(loggedIn);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @Operation(summary = "Delete admin", description = "Deletes an admin by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Admin deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(
        @Parameter(description = "ID of the admin to delete", required = true)
        @PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Admin deleted");
    }
}
