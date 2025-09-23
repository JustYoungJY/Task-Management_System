package app.taskmanagement_system;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("select count(*) from TaskEntity t where t.assignedUserId = :assignedUserId and t.status = 'IN_PROGRESS'")
    int countTasksInProgressForUser(@Param("assignedUserId") Long assignedUserId);

    @Query("""
            Select t from TaskEntity t where
            (:creatorId IS NULL or t.creatorId = :creatorId)
            AND (:assignedUserId IS NULL or t.assignedUserId = :assignedUserId)
            AND (:status IS NULL or t.status = :status)
            AND (:priority IS NULL or t.priority = :priority)
            """)
    List<TaskEntity> findByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") Status status,
            @Param("priority") Priority priority,
            Pageable pageable
    );
}
