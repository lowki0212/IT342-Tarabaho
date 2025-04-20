package tarabaho.tarabaho.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tarabaho.tarabaho.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Worker findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Worker> findByEmail(String email);
    Optional<Worker> findByPhoneNumber(String phoneNumber);
    List<Worker> findAllByEmail(String email);
    List<Worker> findAllByPhoneNumber(String phoneNumber);
    
}
