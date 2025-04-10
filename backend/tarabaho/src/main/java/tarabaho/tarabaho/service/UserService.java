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
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    // Used in /me endpoint
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to save updated user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Used in alternative implementation
    public User updateUserPhone(String email, String phoneNumber) throws Exception {
        User user = findByEmail(email)
            .orElseThrow(() -> new Exception("User not found with email: " + email));
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    public User editUser(Long id, User updatedUser) throws Exception {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new Exception("User not found"));

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setBirthday(updatedUser.getBirthday());
        existingUser.setLocation(updatedUser.getLocation());

        return userRepository.save(existingUser);
    }
}