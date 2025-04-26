package tarabaho.tarabaho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.entity.Booking;
import tarabaho.tarabaho.entity.BookingStatus;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.BookingRepository;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Worker> getWorkersByCategory(String categoryName) {
        return workerRepository.findByCategoryName(categoryName);
    }

    public Worker registerWorker(Worker worker) {
        // Validate new fields
        if (worker.getHourly() == null || worker.getHourly() <= 0) {
            throw new IllegalArgumentException("Hourly rate must be provided and greater than 0.");
        }
        if (worker.getStars() != null && (worker.getStars() < 0 || worker.getStars() > 5)) {
            throw new IllegalArgumentException("Initial stars must be between 0 and 5.");
        }

        // Ensure certificates are properly linked to the worker
        if (worker.getCertificates() != null) {
            worker.getCertificates().forEach(certificate -> certificate.setWorker(worker));
        }
        return workerRepository.save(worker);
    }

    public Worker loginWorker(String username, String password) throws Exception {
        Worker worker = workerRepository.findByUsername(username);
        if (worker != null && worker.getPassword().equals(password)) {
            return worker;
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public void deleteWorker(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new IllegalArgumentException("Worker not found");
        }
        workerRepository.deleteById(id);
    }

    public Worker editWorker(Long id, Worker updatedWorker) throws Exception {
        Worker existingWorker = workerRepository.findById(id)
            .orElseThrow(() -> new Exception("Worker not found"));

        // Update existing fields
        existingWorker.setFirstName(updatedWorker.getFirstName());
        existingWorker.setLastName(updatedWorker.getLastName());
        existingWorker.setUsername(updatedWorker.getUsername());
        existingWorker.setPassword(updatedWorker.getPassword());
        existingWorker.setEmail(updatedWorker.getEmail());
        existingWorker.setPhoneNumber(updatedWorker.getPhoneNumber());
        existingWorker.setAddress(updatedWorker.getAddress());
        existingWorker.setBiography(updatedWorker.getBiography());
        existingWorker.setBirthday(updatedWorker.getBirthday());
        existingWorker.setProfilePicture(updatedWorker.getProfilePicture());

        // Update new fields
        if (updatedWorker.getHourly() != null) {
            if (updatedWorker.getHourly() <= 0) {
                throw new IllegalArgumentException("Hourly rate must be greater than 0.");
            }
            existingWorker.setHourly(updatedWorker.getHourly());
        }
        if (updatedWorker.getIsAvailable() != null) {
            existingWorker.setIsAvailable(updatedWorker.getIsAvailable());
        }
        if (updatedWorker.getIsVerified() != null) {
            existingWorker.setIsVerified(updatedWorker.getIsVerified());
        }
        if (updatedWorker.getLatitude() != null) {
            existingWorker.setLatitude(updatedWorker.getLatitude());
        }
        if (updatedWorker.getLongitude() != null) {
            existingWorker.setLongitude(updatedWorker.getLongitude());
        }
        if (updatedWorker.getAverageResponseTime() != null) {
            existingWorker.setAverageResponseTime(updatedWorker.getAverageResponseTime());
        }

        // Stars and ratingCount are updated via updateRating
        return workerRepository.save(existingWorker);
    }

    public Worker updateRating(Long workerId, Long bookingId, Double newRating, Long userId) throws Exception {
    if (newRating < 1.0 || newRating > 5.0) {
        throw new IllegalArgumentException("Rating must be between 1.0 and 5.0.");
    }
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new Exception("Booking not found"));
    if (booking.getStatus() != BookingStatus.COMPLETED) {
        throw new Exception("Booking must be completed to submit a rating");
    }
    if (!booking.getUser().getId().equals(userId)) {
        throw new Exception("Only the booking user can submit a rating");
    }
    if (!booking.getWorker().getId().equals(workerId)) {
        throw new Exception("Worker does not match the booking");
    }
    Worker worker = workerRepository.findById(workerId)
        .orElseThrow(() -> new Exception("Worker not found"));
    int currentCount = worker.getRatingCount();
    double currentStars = worker.getStars();
    double totalStars = currentStars * currentCount + newRating;
    int newCount = currentCount + 1;
    double newAverage = totalStars / newCount;
    worker.setStars(Math.round(newAverage * 10.0) / 10.0);
    worker.setRatingCount(newCount);
    return workerRepository.save(worker);
}

    public Optional<Worker> findByUsername(String username) {
        return Optional.ofNullable(workerRepository.findByUsername(username));
    }

    public Optional<Worker> findByEmail(String email) {
        List<Worker> workers = workerRepository.findAllByEmail(email);
        if (workers.size() > 1) {
            return Optional.empty(); // Treat multiple results as duplicate
        }
        return workers.isEmpty() ? Optional.empty() : Optional.of(workers.get(0));
    }

    public Optional<Worker> findByPhoneNumber(String phoneNumber) {
        List<Worker> workers = workerRepository.findAllByPhoneNumber(phoneNumber);
        if (workers.size() > 1) {
            return Optional.empty(); // Treat multiple results as duplicate
        }
        return workers.isEmpty() ? Optional.empty() : Optional.of(workers.get(0));
    }

    public Worker findById(Long workerId) {
        System.out.println("WorkerService: Finding worker by ID: " + workerId);
        return workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found with ID: " + workerId));
    }
    public Worker updateWorker(Worker worker) {
        // Hash password if provided
        return workerRepository.save(worker);
    }
    // Added methods for booking system compatibility
    public List<Worker> getAvailableWorkers() {
        return workerRepository.findAllAvailable();
    }

    public List<Worker> getWorkersByMinimumStars(Double minStars) {
        return workerRepository.findByMinimumStars(minStars);
    }

    public List<Worker> getWorkersByMaxHourly(Double maxHourly) {
        return workerRepository.findByMaxHourly(maxHourly);
    }

    public List<Worker> getAvailableWorkersByCategory(String categoryName) {
        return workerRepository.findAvailableWorkersByCategory(categoryName);
    }

    public List<Worker> getNearbyAvailableWorkersByCategory(String categoryName, Double latitude, Double longitude, Double radius) {
        return workerRepository.findNearbyAvailableWorkersByCategory(categoryName, latitude, longitude, radius);
    }

    public List<Worker> findNearbyWorkersForUrgentJob(String categoryName, Double latitude, Double longitude, Double radius) {
        // Validate inputs
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (latitude == null || longitude == null || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid latitude or longitude values");
        }
        if (radius == null || radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }
    
        // Reuse the existing repository method
        return workerRepository.findNearbyAvailableWorkersByCategory(categoryName, latitude, longitude, radius);
    }
    
}