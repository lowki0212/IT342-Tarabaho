package tarabaho.tarabaho.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    
    public Admin registerAdmin(Admin admin) throws Exception {
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new Exception("Email already exists");
        }
        return adminRepository.save(admin);
    }

    public Admin loginAdmin(String username, String password) throws Exception {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin not found");
        }
        adminRepository.deleteById(id);
    }

    public Admin editAdmin(Long id, Admin updatedAdmin) throws Exception {
        Admin existingAdmin = adminRepository.findById(id)
            .orElseThrow(() -> new Exception("Admin not found"));
        
        // Update fields (avoid updating ID)
        existingAdmin.setFirstname(updatedAdmin.getFirstname());
        existingAdmin.setLastname(updatedAdmin.getLastname());
        existingAdmin.setUsername(updatedAdmin.getUsername());
        existingAdmin.setPassword(updatedAdmin.getPassword());
        existingAdmin.setEmail(updatedAdmin.getEmail());
        existingAdmin.setAddress(updatedAdmin.getAddress());
        existingAdmin.setProfilePicture(updatedAdmin.getProfilePicture()); // Update profile picture
        
        // Check for duplicates (excluding this admin)
        Admin byUsername = adminRepository.findByUsername(updatedAdmin.getUsername());
        if (byUsername != null && !byUsername.getId().equals(id)) {
            throw new Exception("Username already exists");
        }
        Admin byEmail = adminRepository.findByEmail(updatedAdmin.getEmail());
        if (byEmail != null && !byEmail.getId().equals(id)) {
            throw new Exception("Email already exists");
        }
        
        return adminRepository.save(existingAdmin);
    }

    public Admin updateProfilePicture(Long id, String filePath) throws Exception {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new Exception("Admin not found"));
        admin.setProfilePicture(filePath);
        return adminRepository.save(admin);
    }
}