package com.teamvoy.shop.security;

import com.teamvoy.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserSecurity loadUserByUsername(String email) throws UsernameNotFoundException {
        return UserSecurity.fromUserToCustomUserDetails(
                userRepository.findByEmail(email).orElseThrow(() -> {
                    throw new UsernameNotFoundException("User with email: " + email + " is not found!");
                })
        );
    }
}
