package com.example.service;

import com.example.models.dto.UserDto;
import com.example.models.entity.PetEntity;
import com.example.models.entity.UserEntity;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        UserEntity user = optionalUser.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.delete(user);
        return user;
    }

    public UserEntity userLogin(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь не найден"));

        if (password.equals(user.getPassword())) {
            return user;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Неверный пароль");
        }
    }

    public UserEntity createUser(UserEntity user) {
        UserEntity existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new IllegalArgumentException("Пользователь с таким именем пользователя уже существует: " + user.getUsername());
        }

        return userRepository.save(user);
    }


}
