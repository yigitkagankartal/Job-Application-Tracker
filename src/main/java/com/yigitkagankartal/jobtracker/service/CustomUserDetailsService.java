package com.yigitkagankartal.jobtracker.service;

import com.yigitkagankartal.jobtracker.model.AppUser;
import com.yigitkagankartal.jobtracker.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("USER FROM DB: " + appUser.getUsername());
        System.out.println("ROLE FROM DB: " + appUser.getRole());
        System.out.println("HASH FROM DB: " + appUser.getPassword());

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities("ROLE_" + appUser.getRole())
                .build();
    }

}
