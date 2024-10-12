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
import java.nio.charset.StandardCharsets;
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
        CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);
        String requestURI = request.getRequestURI();
        logger.debug("Request URI: {}"+ requestURI);

        if (requestURI.equals("/graphql") && isPublicGraphqlOperation(cachedBodyRequest)) {
            logger.debug("Public GraphQL operation detected. Skipping authentication.");
            filterChain.doFilter(cachedBodyRequest, response);  // Skip JWT authentication for public GraphQL operations
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
        //HttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);

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
                    logger.debug("User authenticated: {}"+ userMail);
                }
                else {
                    logger.warn("Invalid JWT token for user: {}"+ userMail); // Log token validation failure
                }
            }
        }else {
            logger.warn("Authorization header is missing or malformed"); // Log missing header
        }

        // Pass the wrapped request to the next filter in the chain
        filterChain.doFilter(cachedBodyRequest, response);
    }
    private boolean isPublicGraphqlOperation(CachedBodyHttpServletRequest request) {
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.debug("Request Body: {}"+ requestBody);
        return requestBody.contains("registerUser") || requestBody.contains("registerAdmin");
    }
}

