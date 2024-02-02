package com.example.service;

import com.example.mapping.UserMapping;
import com.example.models.dto.UserDto;
import com.example.models.entity.RoleEntity;
import com.example.models.entity.UserEntity;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapping userMapping;


    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapping.dtoToEntity(userDto);
        return userMapping.entityToDto(
                userRepository.save(userEntity)
        );
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
        role.setName("MANAGER");
        user.setRole(role);

        return userRepository.save(user);
    }
}
