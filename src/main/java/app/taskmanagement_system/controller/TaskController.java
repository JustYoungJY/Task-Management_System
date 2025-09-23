package app.taskmanagement_system.controller;

import app.taskmanagement_system.Priority;
import app.taskmanagement_system.Status;
import app.taskmanagement_system.Task;
import app.taskmanagement_system.TaskSearchFilter;
import app.taskmanagement_system.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/tasks")
@RestController
public class TaskController {

    private static final Logger logger = Logger.getLogger(TaskController.class.getName());
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Handling GET request for /tasks/" + id);
        Task task = taskService.getTaskById(id);
        logger.info("Finished handling GET request for /tasks/" + id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        logger.info("Handling POST request for /tasks");
        Task newTask = taskService.createTask(task);
        logger.info("Finished handling POST request for /tasks");
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid Task task) {
        logger.info("Handling PUT request for /tasks/" + id);
        Task updatedTask = taskService.updateTask(id, task);
        logger.info("Finished handling PUT request for /tasks/" + id);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Handling DELETE request for /tasks/" + id);
        taskService.deleteTask(id);
        logger.info("Finished handling DELETE request for /tasks/" + id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable Long id) {
        logger.info("Handling POST request for /tasks/" + id);
        Task startedTask = taskService.startTask(id);
        logger.info("Finished handling POST request for /tasks/" + id + "/start");
        return ResponseEntity.ok(startedTask);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        logger.info("Handling POST request for /tasks/" + id);
        Task completedTask = taskService.completeTask(id);
        logger.info("Finished handling POST request for /tasks/" + id);
        return ResponseEntity.ok(completedTask);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            Pageable pageable
    ) {
        logger.info("Handling GET request for /tasks/search");

        TaskSearchFilter taskSearchFilter = new TaskSearchFilter(creatorId, assignedUserId, status, priority);
        List<Task> tasks = taskService.searchTasks(taskSearchFilter, pageable);

        logger.info("Finished handling GET request for /tasks/search");
        return ResponseEntity.ok(tasks);
    }
}
