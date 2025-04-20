package tarabaho.tarabaho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;
       
    public Worker registerWorker(Worker worker) {
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

        // Update certificates

        return workerRepository.save(existingWorker);
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
}