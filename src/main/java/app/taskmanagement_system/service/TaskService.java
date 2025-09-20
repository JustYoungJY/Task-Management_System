package app.taskmanagement_system.service;

import app.taskmanagement_system.Priority;
import app.taskmanagement_system.Status;
import app.taskmanagement_system.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    private final Map<Long, Task> tasks = Map.of(
            1L, new Task(
                    1L, 228L, 337L, Status.CREATED, LocalDateTime.now(),
                    LocalDate.now().plusDays(1), Priority.LOW),
            2L, new Task(
                    2L, 337L, 224L, Status.IN_PROGRESS, LocalDateTime.now(),
                    LocalDate.now().plusDays(2), Priority.MEDIUM),
            3L, new Task(
                    3L, 300L, 200L, Status.IN_PROGRESS, LocalDateTime.now(),
                    LocalDate.now().plusDays(3), Priority.HIGH)
    );  // Some test data

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable("id") Long id) {
        if(!tasks.containsKey(id)) {
            throw new NoSuchElementException("Not found task with id: " + id);
        }

        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

}
