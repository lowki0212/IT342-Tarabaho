package tarabaho.tarabaho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
 // 🔐 Register Admin
    public Admin registerAdmin(Admin admin) throws Exception {
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new Exception("Email already exists");
        }
        return adminRepository.save(admin);
    }

    // 🔑 Login Admin
    public Admin loginAdmin(String username, String password) throws Exception {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        } else {
            throw new Exception("Invalid username or password");
        }
    }
}
