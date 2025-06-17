package com.groupformer.security;

import com.groupformer.model.User;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;
    @Setter
    private Boolean emailVerified;

    public CustomUserDetails(User user) {
        this.user = user;
        this.emailVerified = false;
    }

    public CustomUserDetails(User user, Boolean emailVerified) {
        this.user = user;
        this.emailVerified = emailVerified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (user.getCguAcceptedAt() == null) {
            return false;
        }

        return user.getCguAcceptedAt().isAfter(
                LocalDateTime.now().minusMonths(13)
        );
    }

    @Override
    public boolean isEnabled() {
        return user.getEmailVerified() != null ? user.getEmailVerified() : false;
    }

    public User getUser() {
        return user;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

}