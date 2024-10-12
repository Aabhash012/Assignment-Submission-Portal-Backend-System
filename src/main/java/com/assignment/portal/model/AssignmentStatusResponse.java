package com.assignment.portal.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Document(collection = "assignment status")
@Builder
public class AssignmentStatusResponse {
    private UUID assignmentId;
    private AssignmentStatusEnum status;
    private String message;
}
