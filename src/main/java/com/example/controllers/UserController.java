package com.example.controllers;

import com.example.models.dto.UserDto;
import com.example.models.entity.UserEntity;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "user", description = "Operations about user")
public class UserController {

    private final UserService userService;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Creates list of users with given input array")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "default", description = "successful operation",
                    content = @Content)})

    @PostMapping("/createWithArray")
    @RolesAllowed("ROLE_ADMIN")
    public List<UserEntity> createUserArray(
            @Parameter(description = "Array of user objects")
            @RequestBody UserEntity[] users
    ) {
        return userService.createUsersArray(users);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Creates list of users with given input list")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation", content = @Content)})

    @PostMapping("/createWithList")
    @RolesAllowed("ROLE_ADMIN")
    public List<UserEntity> createUserList(
            @Parameter(description = "List of user objects")
            @RequestBody List<UserEntity> users
    ) {
        return userService.createUsersList(users);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @GetMapping("/{username}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> getUser(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing.")
            @PathVariable("username") String username
    ) {
        Optional<UserEntity> user = userService.getUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Update user by username", description = "This can only be done by the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @PutMapping("/{username}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> putUser(
            @Parameter(description = "Name that needs to be updated")
            @PathVariable("username") String username,
            @Parameter(description = "Updated user object")
            @RequestBody UserEntity updatedUser
    ) {
        UserEntity updateUser = userService.updateUser(username, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Delete user", description = "This can only be done by the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})

    @DeleteMapping("/{username}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "The name that needs to be deleted")
            @PathVariable("username") String username
    ) {
        Optional<UserEntity> deletedUser = userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(deletedUser);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Logs user into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content)})

    @GetMapping("/login")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> userLogin(
            @Parameter(description = "The user name for login")
            @RequestParam("username") String username,
            @Parameter(description = "The password for login in clear text")
            @RequestParam("password") String password
    ) {
        Optional<UserEntity> userEntity = userService.userLogin(username, password);
        return ResponseEntity.status(HttpStatus.OK).body(userEntity);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Logs out current logged in user into session")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @GetMapping("/logout")
    @RolesAllowed("ROLE_ADMIN")
    public UserEntity userLogout() {

        throw new UnsupportedOperationException();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> createUser(
            @Parameter(description = "Created user object")
            @RequestBody UserEntity body
    ) {
        UserEntity createdUser = userService.createUser(body);
        return ResponseEntity.ok(createdUser);
    }
}
