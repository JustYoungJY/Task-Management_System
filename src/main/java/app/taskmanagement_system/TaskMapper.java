package app.taskmanagement_system;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toTask(TaskEntity taskEntity) {
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

    public TaskEntity toTaskEntity(Task task) {
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.localDateTime(),
                task.deadlineDate(),
                task.priority(),
                task.doneDateTime()
        );
    }

}
