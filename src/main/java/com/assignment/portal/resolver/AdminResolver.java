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
public class AdminResolver implements GraphQLQueryResolver,GraphQLMutationResolver {
    @Autowired
    private UserService userService;
    // For admin registration
    public UserDetailsEntity registerAdmin(String userMail, String password) {

        UserDetailsEntity adminDetails = UserDetailsEntity.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ADMIN)
                .userMail(userMail)
                .password(password)
                .build();
        return userService.register(adminDetails);
    }

    // Admin login
    public String login(String userMail, String password) {
//        UserDetails admin = userService.loginUser(userMail, password);
//        if (!admin.getRole().equals(UserRole.ADMIN)) {
//            throw new RuntimeException("Unauthorized");
//        }
        return userService.login(userMail, password);
    }
}


