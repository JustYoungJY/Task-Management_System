package app.taskmanagement_system.service;

import app.taskmanagement_system.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Task getTaskById(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No task found with id " + id));

        return mapper.toTask(taskEntity);
    }

    public Task createTask(Task task) {
        if (task.id() != null) {
            throw new IllegalArgumentException("Task id should be empty");
        }
        if (task.status() != Status.CREATED) {
            throw new IllegalArgumentException("Task status should be empty");
        }

        TaskEntity taskEntity = mapper.toTaskEntity(task);
        taskEntity.setId(null);
        taskEntity.setDoneDateTime(null);
        repository.save(taskEntity);

        return mapper.toTask(taskEntity);
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        TaskEntity oldTask = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No task found with id " + id));

        if (oldTask.getStatus() == Status.DONE && task.status() != Status.IN_PROGRESS) {
            throw new IllegalArgumentException("Task status should not be done.");
        }

        TaskEntity newTask = mapper.toTaskEntity(task);
        newTask.setId(oldTask.getId());
        newTask.setDoneDateTime(null);
        repository.save(newTask);

        return mapper.toTask(newTask);
    }

    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Not found task with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public Task startTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No task found with id " + id));

        if (taskEntity.getAssignedUserId() == null) {
            throw new IllegalArgumentException("Task assigned user id is null");
        }
        if (repository.countTasksInProgressForUser(taskEntity.getAssignedUserId()) >= 4) {
            throw new IllegalArgumentException("Too many tasks in progress for user " + taskEntity.getId());
        }

        taskEntity.setStatus(Status.IN_PROGRESS);
        repository.save(taskEntity);
        return mapper.toTask(taskEntity);
    }

    @Transactional
    public Task completeTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found task with id " + id));

        if (taskEntity.getAssignedUserId() == null) {
            throw new IllegalArgumentException("Task assigned user id is null");
        }
        if (taskEntity.getDeadlineDate() == null) {
            throw new IllegalArgumentException("Task deadline date is null");
        }

        taskEntity.setStatus(Status.DONE);
        taskEntity.setDoneDateTime(LocalDateTime.now());
        repository.save(taskEntity);
        return mapper.toTask(taskEntity);
    }

    public List<Task> searchTasks(TaskSearchFilter taskSearchFilter, Pageable pageable) {
        List<TaskEntity> taskEntities = repository.findByFilter(
                taskSearchFilter.creatorId(),
                taskSearchFilter.assignedUserId(),
                taskSearchFilter.status(),
                taskSearchFilter.priority(),
                pageable
        );

        return taskEntities.stream().map(mapper::toTask).toList();
    }

}
