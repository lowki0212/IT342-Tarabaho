package tarabaho.tarabaho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarabaho.tarabaho.entity.User;
import tarabaho.tarabaho.repository.UserRepository;

import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public User loginUser(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    // ✅ Method used in /me endpoint
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Method to save updated user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Used in alternative implementation
    public User updateUserPhone(String email, String phoneNumber) throws Exception {
        User user = findByEmail(email)
            .orElseThrow(() -> new Exception("User not found with email: " + email));
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }
}
