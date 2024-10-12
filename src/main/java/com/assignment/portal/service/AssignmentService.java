package com.assignment.portal.service;

import com.assignment.portal.exception.AdminNotFoundException;
import com.assignment.portal.exception.AssignmentNotFoundException;
import com.assignment.portal.exception.UserNotFoundException;
import com.assignment.portal.model.*;
import com.assignment.portal.repository.AssignmentRepository;
import com.assignment.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private UserRepository userRepository;

    public AssignmentDetailsForAnAdmin uploadAssignment(UUID userId,UUID adminId,String task) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("No user found with ID: " + userId);
        }

        // Check if the admin exists
        if (!userRepository.existsById(adminId)) {
            throw new AdminNotFoundException("No admin found with ID: " + adminId);
        }
        AssignmentDetailsForAnAdmin assignmentDetails = AssignmentDetailsForAnAdmin.builder()
                .assignmentId(UUID.randomUUID())
                .status(AssignmentStatusEnum.PENDING)
                .submittedAt(LocalDateTime.now())
                .userId(userId)
                .adminId(adminId)
                .task(task)
                .build();
        return assignmentRepository.save(assignmentDetails);
    }

    public List<AssignmentDetailsForAnAdmin> getAllAssignmentsForAnAdmin(UUID adminId) {
        Boolean adminExists = userRepository.existsById(adminId);
        if (!adminExists) {
            throw new AdminNotFoundException("No admin found with ID: " + adminId);
        }
        return assignmentRepository.findByAdminId(adminId);
    }

    public AssignmentStatusResponse updateAssignmentStatus(UUID assignmentId, AssignmentStatusEnum status) {
        AssignmentDetailsForAnAdmin assignment = assignmentRepository.findByAssignmentId(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found with ID: "+ assignmentId));

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

