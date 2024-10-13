package com.assignment.portal.repository;

import com.assignment.portal.model.AdminDetails;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.model.UserDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserDetailsEntity, UUID> {
    Optional<UserDetailsEntity> findByUserMail(String userMail);
    List<AdminDetails> findByRole(UserRole role);
    UserDetailsEntity findUserRoleById(UUID id);
}

