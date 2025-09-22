package app.taskmanagement_system;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task(
        @Null
        Long id,
        @NotNull
        Long creatorId,
        Long assignedUserId,
        Status status,
        LocalDateTime localDateTime,
        @NotNull
        @Future
        LocalDate deadlineDate,
        @NotNull
        Priority priority,
        LocalDateTime doneDateTime
) {
}
