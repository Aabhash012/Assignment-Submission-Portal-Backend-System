package com.assignment.portal.model;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Document(collection = "users")
@Data
@Builder
public class UserDetailsEntity implements UserDetails {
    @Id
    private UUID id;
    private String userMail;
    private String password;
    private UserRole role; // "USER" or "ADMIN"

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert roles or authorities to `GrantedAuthority`
        return Collections.singletonList((GrantedAuthority) () -> "ROLE_" + role.name()); // Adapting role to GrantedAuthority
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userMail; // Assuming email is used as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // You can add your own logic here
    }
}


