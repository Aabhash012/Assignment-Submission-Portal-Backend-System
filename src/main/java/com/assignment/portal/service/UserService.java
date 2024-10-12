package com.assignment.portal.service;

import com.assignment.portal.exception.UserAlreadyExistsException;
import com.assignment.portal.model.AdminDetails;
import com.assignment.portal.model.UserDetailsEntity;
import com.assignment.portal.model.UserRole;
import com.assignment.portal.repository.UserRepository;
import com.assignment.portal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
    private final JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository; // Interacts with the user repository.
    private PasswordEncoder passwordEncoder; // For encrypting passwords before saving them.

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailsEntity register(UserDetailsEntity user) {
        if (userRepository.findByUserMail(user.getUserMail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getUserMail());
       }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String userMail, String password) {
        Optional<UserDetailsEntity> optionalUser = userRepository.findByUserMail(userMail);
        if (optionalUser.isPresent()) {
            UserDetailsEntity user = optionalUser.get();
            // Check if the provided password matches the stored encrypted password.
            if (passwordEncoder.matches(password, user.getPassword())) {
                return  jwtUtil.generateToken(user);
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public List<AdminDetails> getAllAdmins(){
        return userRepository.findByRole(UserRole.ADMIN);
    }

    @Override
    public UserDetailsEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
