package com.rtcc.demo.services;

import com.rtcc.demo.model.Coordinator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailImpl implements UserDetails {

    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailImpl(String id, String name, String username, String password, String email,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserDetailImpl build(Coordinator coordinator) {
//        System.out.println("UserDetail: " + coordinator.getUsername());
//        System.out.println("UserDetail: " + coordinator.getPassword());
//        System.out.println("UserDetail: " + coordinator.getEmail());
//        System.out.println("UserDetail: " + coordinator.getName());
//        System.out.println("UserDetail: " + coordinator.getId());
        return new UserDetailImpl(
                coordinator.getId(),
                coordinator.getName(),
                coordinator.getUsername(),
                coordinator.getPassword(),
                coordinator.getEmail(),
                new ArrayList<>()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
