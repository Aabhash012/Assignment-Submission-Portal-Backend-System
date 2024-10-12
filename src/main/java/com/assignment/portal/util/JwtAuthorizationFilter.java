package com.assignment.portal.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        filterChain.doFilter(cachedBodyRequest, response);
    }
    private boolean isPublicGraphqlOperation(CachedBodyHttpServletRequest request) {
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.debug("Request Body: {}"+ requestBody);
        return requestBody.contains("registerUser") || requestBody.contains("registerAdmin");
    }
}

