package com.example.service;

import com.example.mapping.UserMapping;
import com.example.models.dto.UserDto;
import com.example.models.entity.OrderEntity;
import com.example.models.entity.RoleEntity;
import com.example.models.entity.UserEntity;
import com.example.models.exceptions.GenericBadRequestException;
import com.example.models.exceptions.GenericNotFoundException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.models.enums.ErrorCode.*;

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

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(USER_NOT_FOUND, username));
    }

    public UserEntity updateUser(String username, UserEntity updatedUser) {
        UserEntity existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(USER_NOT_FOUND, username));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setUserStatus(updatedUser.getUserStatus());

        return userRepository.save(existingUser);
    }

    public UserEntity deleteUser(String username) {
        return userRepository.findByUsername(username).map(user -> {
            userRepository.delete(user);
            return user;
        }).orElseThrow(() -> new GenericNotFoundException(USER_NOT_FOUND, username));
    }

    public UserEntity userLogin(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> password.equals(user.getPassword()))
                .orElseThrow(() -> new GenericBadRequestException(USER_BAD_VALUE, username));
    }

    public UserEntity createUser(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(user.getUsername());

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        RoleEntity role = new RoleEntity();
        role.setName("MANAGER");
        user.setRole(role);

        return userRepository.save(user);
    }
}
