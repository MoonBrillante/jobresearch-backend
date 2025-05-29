package com.example.jobresearch.security;


import com.example.jobresearch.domain.models.AppUser;
import com.example.jobresearch.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository repository;
    public UserDetailsServiceImpl(AppUserRepository repository){

        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> user = repository.findByUsername(username);

        User.UserBuilder builder = null;

        if (user.isPresent()) {
            AppUser currentUser = user.get();
            builder = User.withUsername(username);
            builder.password(currentUser.getPassword());
            builder.roles(currentUser.getRoleName());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();

    }
}
