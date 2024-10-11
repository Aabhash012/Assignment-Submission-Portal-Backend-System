package com.assignment.portal.model;

import lombok.Builder;

import java.util.UUID;
@Builder
public class AssignmentStatusResponse {
    private UUID assignmentId;
    private AssignmentStatusEnum status;
    private String message;
}
