package com.assignment.portal.model;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "assignment details")
@Data
@Getter
@Builder
public class AssignmentDetailsForAnAdmin {
    private UUID assignmentId;
    private UUID adminId;
    private UUID userId;
    private String task;
    private LocalDateTime submittedAt;
    private AssignmentStatusEnum status; // "PENDING", "ACCEPTED", "REJECTED"

}

