package com.example.service;

import com.example.models.dto.UserDto;
import com.example.models.entity.RoleEntity;
import com.example.models.entity.UserEntity;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            return new org.springframework.security.core.userdetails.User(
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getRole() != null ?
                            Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getName())) :
                            Collections.emptyList()
            );
        } else {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
        }
    }


    public UserDto save(UserDto userDto) {
        UserEntity userEntity = map(userDto);
        return map(
                userRepository.save(userEntity)
        );
    }

    public UserEntity map(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .userStatus(userDto.getUserStatus())
                .build();
    }

    public UserDto map(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phone(userEntity.getPhone())
                .userStatus(userEntity.getUserStatus())
                .build();
    }

    public List<UserEntity> createUsersArray(UserEntity[] userEntities) {
        for (UserEntity userEntity : userEntities) {
            if (userEntity.getId() == null) {
                userEntity.setId(UUID.randomUUID());
            }
        }
        return userRepository.saveAll(Arrays.asList(userEntities));
    }

    public List<UserEntity> createUsersList(List<UserEntity> userEntities) {
        for (UserEntity userEntity : userEntities) {
            if (userEntity.getId() == null) {
                userEntity.setId(UUID.randomUUID());
            }
        }
        return userRepository.saveAll(userEntities);
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateUser(String username, UserEntity updatedUser) {
        UserEntity existingUser = userRepository.findByUsername(username).orElse(null);

        if (existingUser != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setUserStatus(updatedUser.getUserStatus());

            return userRepository.save(existingUser);
        } else {
            return userRepository.save(updatedUser);
        }
    }

    public Optional<UserEntity> deleteUser(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            userRepository.delete(user);

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> userLogin(String username, String password) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (password.equals(user.getPassword())) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public UserEntity createUser(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        RoleEntity role = new RoleEntity();
        role.setId(UUID.fromString("7d2437ba-5a48-4997-9473-229c68bff871"));
        user.setRole(role);

        return userRepository.save(user);
    }
}
