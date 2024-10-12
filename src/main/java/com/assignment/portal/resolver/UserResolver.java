package com.assignment.portal.resolver;
import com.assignment.portal.model.UserDetailsEntity;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.service.UserService;
//import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
//import graphql.kickstart.tools.GraphQLMutationResolver;

import java.util.UUID;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

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
}




