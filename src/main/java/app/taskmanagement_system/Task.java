package app.taskmanagement_system;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task(
        Long id, Long creatorId, Long assignedUserId, Status status, LocalDateTime localDateTime,
        LocalDate deadlineDate, Priority priority
) {
}
