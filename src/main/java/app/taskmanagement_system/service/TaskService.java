package app.taskmanagement_system.service;

import app.taskmanagement_system.Status;
import app.taskmanagement_system.Task;
import app.taskmanagement_system.TaskEntity;
import app.taskmanagement_system.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task getTaskById(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No task found with id " + id));

        return toDomainTask(taskEntity);
    }

    public List<Task> getAllTasks() {
        List<TaskEntity> taskEntities = repository.findAll();
        return taskEntities.stream()
                .map(taskEntity -> new Task(
                        taskEntity.getId(),
                        taskEntity.getCreatorId(),
                        taskEntity.getAssignedUserId(),
                        taskEntity.getStatus(),
                        taskEntity.getLocalDateTime(),
                        taskEntity.getDeadlineDate(),
                        taskEntity.getPriority(),
                        taskEntity.getDoneDateTime()
                )).toList();
    }

    public Task createTask(Task task) {
        if (task.id() != null) {
            throw new IllegalArgumentException("Task id should be empty");
        }
        if (task.status() != Status.CREATED) {
            throw new IllegalArgumentException("Task status should be empty");
        }

        TaskEntity taskEntity = new TaskEntity(
                null,
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.localDateTime(),
                task.deadlineDate(),
                task.priority(),
                null
        );

        repository.save(taskEntity);
        return toDomainTask(taskEntity);
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        TaskEntity oldTask = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No task found with id " + id));

        if (oldTask.getStatus() == Status.DONE && task.status() != Status.IN_PROGRESS) {
            throw new IllegalArgumentException("Task status should not be done.");
        }

        TaskEntity newTask = new TaskEntity(
                oldTask.getId(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.localDateTime(),
                task.deadlineDate(),
                task.priority(),
                null
        );

        repository.save(newTask);

        return toDomainTask(newTask);
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
        return toDomainTask(taskEntity);
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
        return toDomainTask(taskEntity);
    }

    private Task toDomainTask(TaskEntity taskEntity) {
        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getStatus(),
                taskEntity.getLocalDateTime(),
                taskEntity.getDeadlineDate(),
                taskEntity.getPriority(),
                taskEntity.getDoneDateTime()
        );
    }
}
