package com.assignment.portal.resolver;
import com.assignment.portal.model.*;
import com.assignment.portal.service.AssignmentService;
import com.assignment.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class AssignmentResolver implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired private AssignmentService assignmentService;
    @Autowired private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    public List<AssignmentDetailsForAnAdmin> getAllAssignmentsForAnAdmin(UUID adminId) {
        return assignmentService.getAllAssignmentsForAnAdmin(adminId);
    }

    @PreAuthorize("isAuthenticated()")
    public AssignmentDetailsForAnAdmin uploadAssignment(UUID userId, String task, UUID adminId) {
        AssignmentDetailsForAnAdmin assignmentDetails = AssignmentDetailsForAnAdmin.builder()
                .assignmentId(UUID.randomUUID())
                .status(AssignmentStatusEnum.PENDING)
                .submittedAt(LocalDateTime.now())
                .userId(userId)
                .adminId(adminId)
                .task(task)
                .build();
        return assignmentService.uploadAssignment(assignmentDetails);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AssignmentStatusResponse acceptAssignment(UUID assignmentId) {
        return assignmentService.updateAssignmentStatus(assignmentId, AssignmentStatusEnum.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AssignmentStatusResponse rejectAssignment(UUID assignmentId) {
        return assignmentService.updateAssignmentStatus(assignmentId, AssignmentStatusEnum.REJECTED);
    }

    @PreAuthorize("isAuthenticated()")
    public List<AdminDetails> getAllAdmins() {
        return userService.getAllAdmins();  // Query to fetch users with Role.ADMIN
    }
}

