package com.assignment.portal.resolver;
import com.assignment.portal.model.*;
import com.assignment.portal.service.AssignmentService;
import com.assignment.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

import graphql.schema.DataFetcher;

@Component
public class AssignmentResolver {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    public DataFetcher<List<AssignmentDetailsForAnAdmin>> getAllAssignmentsForAnAdmin() {
        return dataFetchingEnvironment -> {
            String adminIdString = dataFetchingEnvironment.getArgument("adminId");
            UUID adminId = UUID.fromString(adminIdString);
            return assignmentService.getAllAssignmentsForAnAdmin(adminId);
        };
    }

    @PreAuthorize("isAuthenticated()")
    public DataFetcher<AssignmentDetailsForAnAdmin> uploadAssignment() {
        return dataFetchingEnvironment -> {
            UUID userId = UUID.fromString(dataFetchingEnvironment.getArgument("userId"));
            String task = dataFetchingEnvironment.getArgument("task");
            UUID adminId = UUID.fromString(dataFetchingEnvironment.getArgument("adminId"));
            return assignmentService.uploadAssignment(userId,adminId,task);
        };
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DataFetcher<AssignmentStatusResponse> acceptAssignment() {
        return dataFetchingEnvironment -> {
            UUID assignmentId = UUID.fromString(dataFetchingEnvironment.getArgument("assignmentId"));
            return assignmentService.updateAssignmentStatus(assignmentId, AssignmentStatusEnum.ACCEPTED);
        };
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DataFetcher<AssignmentStatusResponse> rejectAssignment() {
        return dataFetchingEnvironment -> {
            UUID assignmentId = UUID.fromString(dataFetchingEnvironment.getArgument("assignmentId"));
            return assignmentService.updateAssignmentStatus(assignmentId, AssignmentStatusEnum.REJECTED);
        };
    }
}
