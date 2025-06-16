package com.groupformer.security;

import com.groupformer.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
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
                java.time.LocalDateTime.now().minusMonths(13)
        );
    }

    @Override
    public boolean isEnabled() {
        return true;

        // TODO: When you add email activation:
        // return user.isActivated();
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getUserRole() {
        return user.getRole().name();
    }
}