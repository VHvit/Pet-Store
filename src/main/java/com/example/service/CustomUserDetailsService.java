package com.example.service;

import com.example.models.entity.UserEntity;
import com.example.models.exceptions.GenericNotFoundException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static com.example.models.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntityOptional = findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(USERNAME_NOT_FOUND, username));

        return new org.springframework.security.core.userdetails.User(
                userEntityOptional.getUsername(),
                userEntityOptional.getPassword(),
                userEntityOptional.getRole() != null ?
                        Collections.singletonList(new SimpleGrantedAuthority(userEntityOptional.getRole().getName())) :
                        Collections.emptyList()
        );
    }

}
