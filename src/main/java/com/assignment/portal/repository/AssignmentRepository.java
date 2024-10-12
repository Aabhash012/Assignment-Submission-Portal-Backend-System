package com.assignment.portal.repository;

import com.assignment.portal.model.AssignmentDetailsForAnAdmin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends MongoRepository<AssignmentDetailsForAnAdmin, UUID> {

    // Find assignments by admin ID
    List<AssignmentDetailsForAnAdmin> findByAdminId(UUID adminId);

    // Find assignment by assignment ID
    Optional<AssignmentDetailsForAnAdmin> findByAssignmentId(UUID assignmentId);
}

