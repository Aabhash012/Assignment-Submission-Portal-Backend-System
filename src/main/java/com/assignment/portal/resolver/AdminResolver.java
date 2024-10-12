package com.assignment.portal.resolver;
import com.assignment.portal.model.UserDetailsEntity;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;
import graphql.schema.DataFetcher;


@Component
public class AdminResolver {

    @Autowired
    private UserService userService;

    public DataFetcher<UserDetailsEntity> registerAdmin() {
        return dataFetchingEnvironment -> {
            String userMail = dataFetchingEnvironment.getArgument("userMail");
            String password = dataFetchingEnvironment.getArgument("password");
            UserDetailsEntity adminDetails = UserDetailsEntity.builder()
                    .id(UUID.randomUUID())
                    .role(UserRole.ADMIN)
                    .userMail(userMail)
                    .password(password)
                    .build();
            return userService.register(adminDetails);
        };
    }
}



