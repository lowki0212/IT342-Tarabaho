package tarabaho.tarabaho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.service.WorkerService;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
@CrossOrigin(origins = "*")
@Tag(name = "Worker Controller", description = "Handles registration, login, and management of workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Operation(summary = "Register new worker", description = "Registers a new worker in the system")
    @ApiResponse(responseCode = "200", description = "Worker registered successfully")
    @PostMapping("/register")
    public Worker registerWorker(@RequestBody Worker worker) {
        return workerService.registerWorker(worker);
    }

    @Operation(summary = "Login worker", description = "Authenticate a worker using username and password")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Worker logged in successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public Worker loginWorker(@RequestBody Worker worker) throws Exception {
        return workerService.loginWorker(worker.getUsername(), worker.getPassword());
    }

    @Operation(summary = "Get all workers", description = "Retrieve a list of all registered workers")
    @ApiResponse(responseCode = "200", description = "List of workers returned successfully")
    @GetMapping("/all")
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @Operation(summary = "Delete worker", description = "Deletes a worker by ID")
    @ApiResponse(responseCode = "200", description = "Worker deleted successfully")
    @DeleteMapping("/{id}")
    public void deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
    }
}
