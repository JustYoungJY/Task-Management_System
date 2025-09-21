package app.taskmanagement_system.service;

import app.taskmanagement_system.Priority;
import app.taskmanagement_system.Status;
import app.taskmanagement_system.Task;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    AtomicLong counter = new AtomicLong();
    private final Map<Long, Task> tasks = new HashMap<>();


    public Task getTaskById(Long id) {
        if(!tasks.containsKey(id)) {
            throw new NoSuchElementException("Not found task with id: " + id);
        }

        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task createTask(Task task) {
        if (task.id() != null) {
            throw new IllegalArgumentException("Task id should be empty");
        }
        if(task.status() != Status.CREATED) {
            throw new IllegalArgumentException("Task status should be empty");
        }

        Task newTask = new Task(
                counter.incrementAndGet(),
                task.creatorId(),
                task.assignedUserId(),
                Status.CREATED,
                task.localDateTime(),
                task.deadlineDate(),
                task.priority()
        );

        tasks.put(newTask.id(), newTask);
        return newTask;
    }

    public Task updateTask(Long id, Task task) {
        if(!tasks.containsKey(id)) {
            throw new NoSuchElementException("Not found task with id: " + id);
        }
        if(tasks.get(id).status() == Status.DONE) {
            throw new IllegalArgumentException("Task already completed");
        }

        Task newTask = new Task(
                tasks.get(id).id(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.localDateTime(),
                task.deadlineDate(),
                task.priority()
        );

        tasks.put(id, newTask);

        return newTask;
    }

    public void deleteTask(Long id) {
        if(!tasks.containsKey(id)) {
            throw new NoSuchElementException("Not found task with id: " + id);
        }
        tasks.remove(id);
    }

}
