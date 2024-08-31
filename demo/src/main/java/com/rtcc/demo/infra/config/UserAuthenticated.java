package com.rtcc.demo.infra.config;

import com.rtcc.demo.model.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import com.rtcc.demo.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserAuthenticated implements UserDetails {
    private final User user;

    public UserAuthenticated(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority(UserRole.ADMIN.getRole()),
                    new SimpleGrantedAuthority(UserRole.COORDINATOR.getRole()),
                    new SimpleGrantedAuthority(UserRole.ACADEMIC.getRole())
            );
        }
        if (user.getRole() == UserRole.COORDINATOR) {
            return List.of(
                    new SimpleGrantedAuthority(UserRole.COORDINATOR.getRole()),
                    new SimpleGrantedAuthority(UserRole.ACADEMIC.getRole())
            );
        }

        return List.of(new SimpleGrantedAuthority(UserRole.ACADEMIC.getRole()));
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEmailVerified();
    }
}
