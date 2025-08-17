package com.example.demo.Entities;

import com.example.demo.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
public class CustomUserDetails implements UserDetails {
    private final User user;
    public CustomUserDetails(User user) { this.user = user; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return java.util.List.of(); }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getEmail(); } // use email to login
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public User getUser() { return user; }
}