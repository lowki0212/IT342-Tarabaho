package tarabaho.tarabaho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.WorkerRepository;

import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;
       
    public Worker registerWorker(Worker worker) {
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

        existingWorker.setName(updatedWorker.getName());
        existingWorker.setUsername(updatedWorker.getUsername());
        existingWorker.setPassword(updatedWorker.getPassword());
        existingWorker.setEmail(updatedWorker.getEmail());
        existingWorker.setPhoneNumber(updatedWorker.getPhoneNumber());
        existingWorker.setBirthday(updatedWorker.getBirthday());

    
        return workerRepository.save(existingWorker);
    }
}
