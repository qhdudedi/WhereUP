package com.project.whereup.user.service;

import com.project.whereup.oauth.PrincipalDetails;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email);

        if (userData == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new PrincipalDetails(userData);
    }
}

