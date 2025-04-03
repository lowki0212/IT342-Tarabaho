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
        workerRepository.deleteById(id);
    }
}
