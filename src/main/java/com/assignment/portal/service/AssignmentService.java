package com.assignment.portal.service;

import com.assignment.portal.model.AssignmentDetailsForAnAdmin;
import com.assignment.portal.model.AssignmentStatusEnum;
import com.assignment.portal.model.AssignmentStatusResponse;
import com.assignment.portal.repository.AssignmentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class to manage the business logic related to Assignments.
 */
@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Uploads a new assignment and saves it to MongoDB.
     */
    public AssignmentDetailsForAnAdmin uploadAssignment(UUID userId,UUID adminId,String task) {
        AssignmentDetailsForAnAdmin assignmentDetails = AssignmentDetailsForAnAdmin.builder()
                .assignmentId(UUID.randomUUID())
                .status(AssignmentStatusEnum.PENDING)
                .submittedAt(LocalDateTime.now())
                .userId(userId)
                .adminId(adminId)
                .task(task)
                .build();
        return assignmentRepository.save(assignmentDetails); // Save to the database.
    }

    /**
     * Retrieves all assignments tagged to a specific admin by their adminId.
     */
    public List<AssignmentDetailsForAnAdmin> getAllAssignmentsForAnAdmin(UUID adminId) {
        return assignmentRepository.findByAdminId(adminId); // Query database for assignments by adminId.
    }

    /**
     * Updates the status of an assignment (e.g., ACCEPTED or REJECTED).
     */
    public AssignmentStatusResponse updateAssignmentStatus(UUID assignmentId, AssignmentStatusEnum status) {
        AssignmentDetailsForAnAdmin assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Update the status and save the assignment back to the database.
        assignment.setStatus(status);
        assignmentRepository.save(assignment);
        String message = status == AssignmentStatusEnum.ACCEPTED ? "Assignment accepted" : "Assignment rejected";

        return AssignmentStatusResponse.builder()
                .assignmentId(assignment.getAssignmentId())
                .status(assignment.getStatus())
                .message(message)
                .build();
    }
}

