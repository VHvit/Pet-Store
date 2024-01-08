package com.example.controllers;

import com.example.models.entity.UserEntity;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")

@Tag(name = "user", description = "Operations about user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "default", description = "successful operation",
                    content = @Content)})

    @PostMapping("/createWithArray")
    public List<UserEntity> createUserArray(
            @Parameter(description = "Array of user objects")
            @RequestBody UserEntity[] users) {
        return userService.createUsersArray(users);
    }

    @Operation(summary = "Creates list of users with given input list")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation", content = @Content)})

    @PostMapping("/createWithList")
    public List<UserEntity> createUserList(
            @Parameter(description = "List of user objects")
            @RequestBody List<UserEntity> users) {
        return userService.createUsersList(users);
    }


    @Operation(summary = "Get user bu username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @GetMapping("/{username}")
    public UserEntity getUser(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing.")
            @PathVariable("username") String username) {

        return userService.getUserByUsername(username);
    }

    @Operation(summary = "Get user bu username", description = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @PutMapping("/{username}")
    public UserEntity putUser(
            @Parameter(description = "name that need to be updated")
            @PathVariable("username") String username,
            @Parameter(description = "Updated user object")
            @RequestBody UserEntity updatedUser) {

        return userService.updateUser(username, updatedUser);
    }

    @Operation(summary = "Delete user", description = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @DeleteMapping("/{username}")
    public UserEntity deleteUser(
            @Parameter(description = "The name that needs to be deleted")
            @PathVariable("username") String username) {

        return userService.deleteUser(username);
    }

    @Operation(summary = "Logs user into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content)})

    @GetMapping("/login")
    public UserEntity userLogin(
            @Parameter(description = "The user name for login")
            @RequestParam("username") String username,
            @Parameter(description = "The password for login in clear text")
            @RequestParam("password") String password) {

        return userService.userLogin(username, password);
    }

    @Operation(summary = "Logs out current logged in user into session")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @GetMapping("/logout")
    public UserEntity userLogout() {

        throw new UnsupportedOperationException();
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @PostMapping
    public UserEntity createUser(
            @Parameter(description = "Created user object")
            @RequestBody UserEntity body) {
        return userService.createUser(body);
    }
}
