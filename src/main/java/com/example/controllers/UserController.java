package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.models.entity.UserEntity;
import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import com.example.models.ApiErrorResponse;
import com.example.service.UserService;
import org.webjars.NotFoundException;

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

    public ResponseEntity<ApiErrorResponse> userHandleException(HttpStatus status, String message) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse()
                .setCode(status.value())
                .setMessage(message);
        return ResponseEntity.status(status).body(apiErrorResponse);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Creates list of users with given input list")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation", content = @Content)})

    @PostMapping("/createWithList")
    public List<UserEntity> createUserList(
            @Parameter(description = "List of user objects")
            @RequestBody List<UserEntity> users) {
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
    public ResponseEntity<?> getUser(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing.")
            @PathVariable("username") String username) {
        try {
            UserEntity user = userService.getUserByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException ex) {
            return handleGetUserValidationException(ex);
        } catch (NotFoundException ex) {
            return handleGetUserNotFoundException(ex);
        }
    }

    @ExceptionHandler(ValidationException.class) // 400 get user by username
    public ResponseEntity<ApiErrorResponse> handleGetUserValidationException(ValidationException ex) {
        return userHandleException(HttpStatus.BAD_REQUEST, "Invalid username supplied");
    }

    @ExceptionHandler(NotFoundException.class) // 404 get user by username
    public ResponseEntity<ApiErrorResponse> handleGetUserNotFoundException(NotFoundException ex) {
        return userHandleException(HttpStatus.NOT_FOUND, "User not found");
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
    public ResponseEntity<?> putUser(
            @Parameter(description = "Name that needs to be updated")
            @PathVariable("username") String username,
            @Parameter(description = "Updated user object")
            @RequestBody UserEntity updatedUser) {
        try {
            UserEntity updatedUserInfo = userService.updateUser(username, updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUserInfo);
        } catch (ValidationException ex) {
            return handleUpdateUserValidationException(ex);
        } catch (NotFoundException ex) {
            return handleUpdateUserNotFoundException(ex);
        }
    }

    @ExceptionHandler(ValidationException.class) // 400 update user by username
    public ResponseEntity<ApiErrorResponse> handleUpdateUserValidationException(ValidationException ex) {
        return userHandleException(HttpStatus.BAD_REQUEST, "Invalid username supplied");
    }

    @ExceptionHandler(NotFoundException.class) // 404 update user by username
    public ResponseEntity<ApiErrorResponse> handleUpdateUserNotFoundException(NotFoundException ex) {
        return userHandleException(HttpStatus.NOT_FOUND, "User not found");
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
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "The name that needs to be deleted")
            @PathVariable("username") String username) {
        try {
            UserEntity deletedUser = userService.deleteUser(username);
            return ResponseEntity.status(HttpStatus.OK).body(deletedUser);
        } catch (ValidationException ex) {
            return handleDeleteUserValidationException(ex);
        } catch (NotFoundException ex) {
            return handleDeleteUserNotFoundException(ex);
        }
    }

    @ExceptionHandler(ValidationException.class) // 400 delete user
    public ResponseEntity<ApiErrorResponse> handleDeleteUserValidationException(ValidationException ex) {
        return userHandleException(HttpStatus.BAD_REQUEST, "Invalid username supplied");
    }

    @ExceptionHandler(NotFoundException.class) // 404 delete user
    public ResponseEntity<ApiErrorResponse> handleDeleteUserNotFoundException(NotFoundException ex) {
        return userHandleException(HttpStatus.NOT_FOUND, "User not found");
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Logs user into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content)})
    @GetMapping("/login")
    public ResponseEntity<?> userLogin(
            @Parameter(description = "The user name for login")
            @RequestParam("username") String username,
            @Parameter(description = "The password for login in clear text")
            @RequestParam("password") String password) {
        try {
            UserEntity loggedInUser = userService.userLogin(username, password);
            return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
        } catch (ValidationException ex) {
            return handleLogsUserValidationException(ex);
        }
    }

    @ExceptionHandler(ValidationException.class) // 400 user login
    public ResponseEntity<ApiErrorResponse> handleLogsUserValidationException(ValidationException ex) {
        return userHandleException(HttpStatus.BAD_REQUEST, "Invalid username supplied");
    }

    private ResponseEntity<ApiErrorResponse> handleException(HttpStatus status, String defaultErrorMessage, String customMessage) {
        String message = customMessage != null ? customMessage : defaultErrorMessage;

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse()
                .setCode(status.value())
                .setMessage(message);
        return ResponseEntity.status(status).body(apiErrorResponse);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Logs out current logged in user into session")
    @ApiResponses(value = {
            @ApiResponse(description = "successful operation",
                    content = @Content)})

    @GetMapping("/logout")
    public UserEntity userLogout() {

        throw new UnsupportedOperationException();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
