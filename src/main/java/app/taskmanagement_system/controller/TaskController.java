package app.taskmanagement_system.controller;

import app.taskmanagement_system.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import app.taskmanagement_system.Task;

@RequestMapping("/tasks")
@RestController
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = Logger.getLogger(TaskController.class.getName());

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("Handling GET request for /tasks");

        List<Task> allTasks = new ArrayList<>(taskService.getAllTasks());

        logger.info("Finished handling GET request for /tasks. Returned " + allTasks.size() + " tasks");

        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Handling GET request for /tasks/" + id);
        try {
            Task task = taskService.getTaskById(id);
            logger.info("Finished handling GET request for /tasks/" + id);
            return ResponseEntity.ok(task);
        } catch (NoSuchElementException e) {
            logger.info("Task with id " + id + " not found");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Handling POST request for /tasks");
        try {
            Task newTask = taskService.createTask(task);
            logger.info("Finished handling POST request for /tasks");
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        } catch (IllegalArgumentException e) {
            logger.info("Illegal argument exception handling POST request for /tasks");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        logger.info("Handling PUT request for /tasks/" + id);
        try {
            Task updatedTask = taskService.updateTask(id, task);
            logger.info("Finished handling PUT request for /tasks/" + id);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            logger.info("Exception handling PUT request for /tasks/" + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Handling DELETE request for /tasks/" + id);
        try {
            taskService.deleteTask(id);
            logger.info("Finished handling DELETE request for /tasks/" + id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            logger.info("Task with id " + id + " not found");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
