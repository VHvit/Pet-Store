package com.example.service;

import com.example.models.dto.UserDto;
import com.example.models.entity.RoleEntity;
import com.example.models.entity.UserEntity;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto save(UserDto userDto) {
        UserEntity userEntity = map(userDto);
        return map(
                userRepository.save(userEntity)
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByLoginAndPassword(String username, String password) {
        Optional<UserEntity> userEntity = findByUsername(username);
        if (userEntity.isPresent()) {
            if (passwordEncoder.matches(password, userEntity.get().getPassword())) {
                return userEntity;
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with username '%s' not found", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole() != null ?
                        Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getName())) :
                        Collections.emptyList()
        );
    }

    public void createNewUser(UserEntity userEntity) {
        RoleEntity role = roleRepository.findByName("guest");

        if (role != null) {
            userEntity.setRole(role);
            userRepository.save(userEntity);
        } else {
            throw new RuntimeException("Role 'guest' not found");
        }
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

    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
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

    public UserEntity deleteUser(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
        return user;
    }

    public UserEntity userLogin(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (password.equals(user.getPassword())) {
            return user;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid password");
        }
    }

    public UserEntity createUser(UserEntity user) {
        UserEntity existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new IllegalArgumentException("A user with that username already exists: " + user.getUsername());
        }

        return userRepository.save(user);
    }


}
