package com.example.demo.controllers;

import com.example.demo.BaseTest;
import com.example.models.ApiErrorResponse;
import com.example.models.dto.UserDto;
import com.example.models.entity.UserEntity;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private UserDto createUserDto(UUID userId, Consumer<UserDto> modifier) {
        UserDto userDto = UserDto.builder()
                .id(userId)
                .email("email@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .phone("+79493164510")
                .userStatus(1)
                .username("hvit")
                .build();
        modifier.accept(userDto);
        return userDto;
    }

    private UserEntity createUser(UUID userId, Consumer<UserEntity> modifier) {
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .email("email@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .phone("+79493164510")
                .userStatus(1)
                .username("hvit")
                .build();
        modifier.accept(userEntity);
        return userRepository.save(userEntity);
    }

    @Test
    void testAddNewUserArray() {
        UUID userId = UUID.randomUUID();
        UserEntity newUser = createUser(userId, p -> {
            p.setFirstName("firstname");
            p.setLastName("lastname");
            p.setUsername("username");
            p.setPassword("password");
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserEntity[]> requestEntity = new HttpEntity<>(new UserEntity[]{newUser}, headers);

        ResponseEntity<UserDto[]> responseEntity = restTemplate.exchange(
                "/user/createWithArray",
                HttpMethod.POST,
                requestEntity,
                UserDto[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        UserDto[] response = responseEntity.getBody();

        assertNotNull(response);
        assertNotEquals(0, response.length);

        UserDto createdUser = response[0];
        assertNotNull(createdUser.getId());
    }

    @Test
    void testAddNewUserList() {
        UUID userId = UUID.randomUUID();
        UserEntity newUser = createUser(userId, p -> {
            p.setFirstName("firstname");
            p.setLastName("lastname");
            p.setUsername("username");
            p.setPassword("password");
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserEntity[]> requestEntity = new HttpEntity<>(new UserEntity[]{newUser}, headers);

        ResponseEntity<List<UserDto>> responseEntity = restTemplate.exchange(
                "/user/createWithArray",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<UserDto>>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<UserDto> response = responseEntity.getBody();

        assertNotNull(response);
        assertFalse(response.isEmpty());

        UserDto createdUser = response.get(0);
        assertNotNull(createdUser.getId());
    }

    @Test
    void testFindUserByUsername() {
        String username = "hvit";
        UserEntity userName = createUser(UUID.randomUUID(), p -> p.setUsername(username));

        ResponseEntity<UserDto> responseValid = restTemplate.exchange(
                "/user/{username}", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                }, username);

        assertEquals(HttpStatus.OK, responseValid.getStatusCode());
        assertNotNull(responseValid.getBody());

        UserDto userDto = responseValid.getBody();
        assertEquals(userName.getId(), userDto.getId());
        assertEquals(userName.getUsername(), userDto.getUsername());
    }

    @Test
    void testFindUserByUsernameBadRequest() {
        String username = null;

        ResponseEntity<ApiErrorResponse> responseInvalid = restTemplate.exchange(
                "/user/{username}", HttpMethod.GET,
                null, ApiErrorResponse.class, username);

        assertEquals(HttpStatus.BAD_REQUEST, responseInvalid.getStatusCode());
        assertNotNull(responseInvalid.getBody());
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        String username = "username";
        UserEntity updateUser = createUser(userId, p -> {
            p.setFirstName("newFirstname");
            p.setLastName("newLastname");
            p.setPassword("newpassword");
        });

        ResponseEntity<UserDto> response = restTemplate.exchange("/user/{username}", HttpMethod.PUT,
                new HttpEntity<>(updateUser), UserDto.class, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }


    @Test
    void testUpdateUserNotFound() {
        UUID userId = UUID.randomUUID();
        String username = "username";
        UserEntity updateUser = createUser(userId, p -> {
            p.setFirstName("newFirstname");
            p.setLastName("newLastname");
            p.setPassword("newpassword");
        });

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/user/{username}", HttpMethod.PUT,
                new HttpEntity<>(updateUser), UserDto.class, username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody().getId());
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        String username = "username";
        UserDto originalUser = createUserDto(userId, p -> p.setUsername("username").setPassword("password"));

        ResponseEntity<UserDto> createResponse = restTemplate.postForEntity("/user", originalUser, UserDto.class);

        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/user/{username}", HttpMethod.DELETE,
                new HttpEntity<>(headers), UserDto.class, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteUserNotFound() {
        String username = "username";
        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", apiKey);

        try {
            restTemplate.exchange(
                    "/user/{username}", HttpMethod.DELETE,
                    new HttpEntity<>(headers), UserDto.class, username);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testUserLogin() {
        String username = "username";
        String password = "password";
        UUID userId = UUID.randomUUID();

        UserDto newUser = createUserDto(userId, p -> {
            p.setFirstName("firstname");
            p.setLastName("lastname");
            p.setUsername("username");
            p.setPassword("password");
        });

        ResponseEntity<UserDto> createResponse = restTemplate.postForEntity("/user", newUser, UserDto.class);

        ResponseEntity<UserDto> response = restTemplate.getForEntity(
                "/user/login?username={username}&password={password}", UserDto.class, username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(username, response.getBody().getUsername());
    }


    @Test
    void testAddNewPet() {
        UUID userId = UUID.randomUUID();
        UserDto newUser = createUserDto(userId, p -> {
            p.setFirstName("newFirstname");
            p.setLastName("newLastname");
            p.setUsername("newusername");
            p.setPassword("newpassword");
        });

        ResponseEntity<UserDto> response = restTemplate.postForEntity("/user", newUser, UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

}
