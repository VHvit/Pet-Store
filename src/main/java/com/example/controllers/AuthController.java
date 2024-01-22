package com.example.controllers;


import com.example.jwt.JwtProvider;
import com.example.models.ApiErrorResponse;
import com.example.models.dto.JwtRequest;
import com.example.models.dto.JwtResponse;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "user", description = "Operations about user")

public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtProvider.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}
