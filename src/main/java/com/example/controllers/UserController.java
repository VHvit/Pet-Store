package com.example.controllers;

import java.util.List;
import com.example.models.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "Operations about user")
public class UserController {

    @Operation(summary = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "default", description = "successful operation",
                    content = @Content)})

    @PostMapping("/createWithArray")
    public User createArray(
            @Parameter(description = "List of user object") @RequestBody List<User> users) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation", content = @Content)})

    @PostMapping("/createWithList")
    public User createList(
            @Parameter(description = "List of user object") @RequestBody List<User> users) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Get user bu username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @GetMapping("/{username}")
    public User getUser(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing.")
            @PathVariable("username") String username) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Get user bu username", description = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @PutMapping("/{username}")
    public User putUser(
            @Parameter(description = "name that need to be updated")
            @PathVariable("username") String username,
            @Parameter(description = "Updated user object")
            @RequestBody User body) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Delete user", description = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @DeleteMapping("/{username}")
    public User deleteUser(
            @Parameter(description = "The name that needs to be deleted")
            @PathVariable("username") String username) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Logs user into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content)})

    @GetMapping("/login")
    public User userLogin(
            @Parameter(description = "The user name for login")
            @RequestParam("username") String username,
            @Parameter(description = "The password for login in clear text")
            @RequestParam("password") String password) {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Logs out current logged in user into session")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @GetMapping("/logout")
    public User userLogout() {

        throw new UnsupportedOperationException();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @PostMapping
    public User createUser(
            @Parameter(description = "Created user object")
            @RequestBody User body) {
        throw new UnsupportedOperationException();
    }
}
