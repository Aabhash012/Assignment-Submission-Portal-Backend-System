package com.assignment.portal.resolver;
import com.assignment.portal.model.AdminDetails;
import com.assignment.portal.model.UserDetailsEntity;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

import graphql.schema.DataFetcher;


@Component
public class UserResolver {

    private final UserService userService;

    @Autowired
    public UserResolver(UserService userService) {
        this.userService = userService;
    }

    // For user registration
    public DataFetcher<UserDetailsEntity> registerUser() {
        return dataFetchingEnvironment -> {
            String userMail = dataFetchingEnvironment.getArgument("userMail");
            String password = dataFetchingEnvironment.getArgument("password");
            UserDetailsEntity userDetailsEntity = UserDetailsEntity.builder()
                    .id(UUID.randomUUID())
                    .role(UserRole.USER)
                    .userMail(userMail)
                    .password(password)
                    .build();
            return userService.register(userDetailsEntity);
        };
    }

    // User login
    public DataFetcher<String> login() {
        return dataFetchingEnvironment -> {
            String userMail = dataFetchingEnvironment.getArgument("userMail");
            String password = dataFetchingEnvironment.getArgument("password");
            return userService.login(userMail, password);
        };
    }
    @PreAuthorize("isAuthenticated()")
    public DataFetcher<List<AdminDetails>> getAllAdmins() {
        return dataFetchingEnvironment -> userService.getAllAdmins();
    }
}




