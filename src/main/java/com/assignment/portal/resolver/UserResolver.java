package com.assignment.portal.resolver;
import com.assignment.portal.model.UserDetailsEntity;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import graphql.kickstart.tools.GraphQLMutationResolver;

import java.util.UUID;

@Component
public class UserResolver implements GraphQLQueryResolver,GraphQLMutationResolver {
    private final UserService userService;

    // Constructor-based injection (better than field injection)
    @Autowired
    public UserResolver(UserService userService) {
        this.userService = userService;
    }
    // For user registration
    public UserDetailsEntity registerUser(String userMail, String password) {

        UserDetailsEntity userDetailsEntity = UserDetailsEntity.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .userMail(userMail)
                .password(password)
                .build();// Default role for users
        return userService.register(userDetailsEntity);
    }

    // User login
    public String login(String userMail, String password) {
        return userService.login(userMail, password);
    }
}



