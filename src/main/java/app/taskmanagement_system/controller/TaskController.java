package app.taskmanagement_system.controller;

import app.taskmanagement_system.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.taskmanagement_system.Task;

@RestController
public class TaskController {

    private final TaskService taskService;
    private static final Logger logger = Logger.getLogger(TaskController.class.getName());

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        logger.info("Handling GET request for /tasks");

        List<Task> allTasks = new ArrayList<>(taskService.getAllTasks());

        logger.info("Finished handling GET request for /tasks. Returned " + allTasks.size() + " tasks");

        return allTasks;
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        logger.info("Handling GET request for /tasks/" + id);

        Task task = taskService.getTaskById(id);

        logger.info("Finished handling GET request for /tasks/" + id + ". Task found: " + (task != null));

        return task;
    }
}
