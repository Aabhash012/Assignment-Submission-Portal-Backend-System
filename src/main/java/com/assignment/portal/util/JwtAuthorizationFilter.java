package com.assignment.portal.util;

import com.assignment.portal.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.Filter;

import java.io.IOException;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/graphql") && isPublicGraphqlOperation(request)) {
            filterChain.doFilter(request, response);  // Skip JWT authentication for public GraphQL operations
            return;
        }
//        String authorizationHeader = request.getHeader("Authorization");
//
//        // Check if the Authorization header exists and starts with "Bearer "
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7); // Extract the token from the header
//            try {
//                String userMail = jwtUtil.extractUsername(token); // Extract the username from the token
//
//                // If userMail is not null and there is no existing authentication in the context
//                if (userMail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(userMail); // Load user details
//
//                    // If token is valid
//                    if (jwtUtil.validateToken(token, userDetails)) {
//                        // Create an authentication object and set it in the security context
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                }
//            } catch (Exception e) {
//                // Handle token validation errors (e.g., token expired, invalid signature)
//                logger.error("Could not authenticate user with the provided JWT", e);
//            }
//        }
//
//        // Continue with the next filter in the chain
//        filterChain.doFilter(request, response);
        HttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);

        String authorizationHeader = cachedBodyRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);

            if (userMail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userMail);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // Pass the wrapped request to the next filter in the chain
        filterChain.doFilter(cachedBodyRequest, response);
    }
    private boolean isPublicGraphqlOperation(HttpServletRequest request) {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            if (requestBody.contains("registerUser") || requestBody.contains("registerAdmin")) {
                return true; // These operations should bypass authentication
            }
        } catch (IOException e) {
            logger.error("Error reading request body", e);
        }
        return false;
    }
}

