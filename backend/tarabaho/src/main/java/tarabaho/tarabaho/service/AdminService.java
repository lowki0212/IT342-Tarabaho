package tarabaho.tarabaho.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.dto.WorkerUpdateDTO;
import tarabaho.tarabaho.entity.Admin;
import tarabaho.tarabaho.entity.Category;
import tarabaho.tarabaho.entity.Certificate;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.AdminRepository;
import tarabaho.tarabaho.repository.CategoryRepository;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Optional<User> findUserById(Long id) {
        return userService.findById(id);
    }

    public Admin registerAdmin(Admin admin) throws Exception {
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            throw new Exception("Email already exists");
        }
        // Hash password
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoderService.encodePassword(admin.getPassword()));
        }
        return adminRepository.save(admin);
    }

    public Admin loginAdmin(String username, String password) throws Exception {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && passwordEncoderService.matches(password, admin.getPassword())) {
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
        // Hash password if provided
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoderService.encodePassword(updatedAdmin.getPassword()));
        }
        existingAdmin.setEmail(updatedAdmin.getEmail());
        existingAdmin.setAddress(updatedAdmin.getAddress());
        existingAdmin.setProfilePicture(updatedAdmin.getProfilePicture());

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

    public Worker editWorker(Long id, WorkerUpdateDTO workerDTO) throws Exception {
        Worker existingWorker = workerRepository.findById(id)
            .orElseThrow(() -> new Exception("Worker not found with id: " + id));

        System.out.println("AdminService: Editing worker ID: " + id);

        // Update only the fields provided in the DTO
        if (workerDTO.getEmail() != null && !workerDTO.getEmail().equals(existingWorker.getEmail())) {
            if (workerRepository.findAllByEmail(workerDTO.getEmail()).size() > 0) {
                throw new IllegalArgumentException("Email already exists.");
            }
            existingWorker.setEmail(workerDTO.getEmail());
            System.out.println("AdminService: Updated email to: " + workerDTO.getEmail());
        }

        if (workerDTO.getPhoneNumber() != null && !workerDTO.getPhoneNumber().equals(existingWorker.getPhoneNumber())) {
            if (!workerDTO.getPhoneNumber().isEmpty() && workerRepository.findAllByPhoneNumber(workerDTO.getPhoneNumber()).size() > 0) {
                throw new IllegalArgumentException("Phone number already exists.");
            }
            existingWorker.setPhoneNumber(workerDTO.getPhoneNumber());
            System.out.println("AdminService: Updated phone number to: " + workerDTO.getPhoneNumber());
        }

        if (workerDTO.getAddress() != null) {
            existingWorker.setAddress(workerDTO.getAddress());
            System.out.println("AdminService: Updated address to: " + workerDTO.getAddress());
        }

        if (workerDTO.getBiography() != null) {
            existingWorker.setBiography(workerDTO.getBiography());
            System.out.println("AdminService: Updated biography to: " + workerDTO.getBiography());
        }

        if (workerDTO.getFirstName() != null) {
            existingWorker.setFirstName(workerDTO.getFirstName());
            System.out.println("AdminService: Updated first name to: " + workerDTO.getFirstName());
        }

        if (workerDTO.getLastName() != null) {
            existingWorker.setLastName(workerDTO.getLastName());
            System.out.println("AdminService: Updated last name to: " + workerDTO.getLastName());
        }

        if (workerDTO.getHourly() != null) {
            if (workerDTO.getHourly() <= 0) {
                throw new IllegalArgumentException("Hourly rate must be greater than 0.");
            }
            existingWorker.setHourly(workerDTO.getHourly());
            System.out.println("AdminService: Updated hourly rate to: " + workerDTO.getHourly());
        }

        if (workerDTO.getBirthday() != null && !workerDTO.getBirthday().isEmpty()) {
            try {
                existingWorker.setBirthday(LocalDate.parse(workerDTO.getBirthday()));
                System.out.println("AdminService: Updated birthday to: " + workerDTO.getBirthday());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid birthday format. Use YYYY-MM-DD.");
            }
        }

        // Only update password if explicitly provided and non-empty
        if (workerDTO.getPassword() != null && !workerDTO.getPassword().trim().isEmpty()) {
            String newHashedPassword = passwordEncoderService.encodePassword(workerDTO.getPassword());
            existingWorker.setPassword(newHashedPassword);
            System.out.println("AdminService: Updated password for worker ID: " + id + " to new hash: " + newHashedPassword);
        } else {
            System.out.println("AdminService: Password not updated for worker ID: " + id);
        }

        if (workerDTO.getIsAvailable() != null) {
            existingWorker.setIsAvailable(workerDTO.getIsAvailable());
            System.out.println("AdminService: Updated isAvailable to: " + workerDTO.getIsAvailable());
        }

        if (workerDTO.getIsVerified() != null) {
            existingWorker.setIsVerified(workerDTO.getIsVerified());
            System.out.println("AdminService: Updated isVerified to: " + workerDTO.getIsVerified());
        }

        if (workerDTO.getLatitude() != null) {
            existingWorker.setLatitude(workerDTO.getLatitude());
            System.out.println("AdminService: Updated latitude to: " + workerDTO.getLatitude());
        }

        if (workerDTO.getLongitude() != null) {
            existingWorker.setLongitude(workerDTO.getLongitude());
            System.out.println("AdminService: Updated longitude to: " + workerDTO.getLongitude());
        }

        if (workerDTO.getAverageResponseTime() != null) {
            existingWorker.setAverageResponseTime(workerDTO.getAverageResponseTime());
            System.out.println("AdminService: Updated averageResponseTime to: " + workerDTO.getAverageResponseTime());
        }

        Worker updatedWorker = workerRepository.save(existingWorker);
        System.out.println("AdminService: Worker ID: " + id + " saved successfully");
        return updatedWorker;
    }

    public Worker addCategoriesToWorker(Long workerId, List<Long> categoryIds) throws Exception {
        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new Exception("Worker not found with id: " + workerId));

        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new IllegalArgumentException("One or more category IDs are invalid.");
        }

        // Add new categories, avoiding duplicates
        List<Category> currentCategories = worker.getCategories();
        for (Category category : categories) {
            if (!currentCategories.contains(category)) {
                currentCategories.add(category);
            }
        }
        worker.setCategories(currentCategories);

        return workerRepository.save(worker);
    }

    public List<Certificate> getCertificatesByWorkerId(Long workerId) {
        return certificateService.getCertificatesByWorkerId(workerId);
    }

    public void deleteWorker(Long id) throws Exception {
        if (!workerRepository.existsById(id)) {
            throw new Exception("Worker not found with id: " + id);
        }
        workerRepository.deleteById(id);
    }
}