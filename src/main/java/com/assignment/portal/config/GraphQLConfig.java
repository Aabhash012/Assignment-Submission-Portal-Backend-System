package com.assignment.portal.config;

import com.assignment.portal.resolver.AdminResolver;
import com.assignment.portal.resolver.AssignmentResolver;
import com.assignment.portal.resolver.UserResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(AdminResolver adminResolver, UserResolver userResolver, AssignmentResolver assignmentResolver) {
        return wiringBuilder -> wiringBuilder
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("getAllAssignmentsForAnAdmin", assignmentResolver.getAllAssignmentsForAnAdmin())
                        .dataFetcher("getAllAdmins", assignmentResolver.getAllAdmins())
                        .dataFetcher("login", userResolver.login()))
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("registerUser", userResolver.registerUser())
                        .dataFetcher("registerAdmin", adminResolver.registerAdmin())
                        .dataFetcher("uploadAssignment", assignmentResolver.uploadAssignment())
                        .dataFetcher("acceptAssignment", assignmentResolver.acceptAssignment())
                        .dataFetcher("rejectAssignment", assignmentResolver.rejectAssignment()));
    }
}

