package app.taskmanagement_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("select count(*) from TaskEntity t where t.assignedUserId = :assignedUserId and t.status = 'IN_PROGRESS'")
    int countTasksInProgressForUser(@Param("assignedUserId") Long assignedUserId);
}
