package tarabaho.tarabaho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tarabaho.tarabaho.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Worker findByUsername(String username);
    boolean existsByUsername(String username);

}
