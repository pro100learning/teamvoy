package com.teamvoy.shop.security;

import com.teamvoy.shop.entity.User;
import com.teamvoy.shop.entity.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserSecurity implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private boolean enabled;
    private List<Role> roles;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserSecurity fromUserToCustomUserDetails(User user) {
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.id = user.getId();
        userSecurity.email = user.getEmail();
        userSecurity.password = user.getPassword();
        userSecurity.enabled = user.isEnabled();
        userSecurity.roles = user.getRoles();
        userSecurity.authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return userSecurity;
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
        return email;
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
        return enabled;
    }
}
