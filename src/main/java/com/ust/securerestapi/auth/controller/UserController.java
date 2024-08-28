package com.ust.securerestapi.auth.controller;

import com.ust.securerestapi.auth.domain.AppUser;
import com.ust.securerestapi.auth.domain.AppUserRole;
import com.ust.securerestapi.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegisterRequest request) {
        var user = new AppUser();
        user.setUserName(request.userName());
        user.setPassword(passwordEncoder.encode(request.password()));
        Set<AppUserRole> roles = Arrays.stream(request.roles())
                                .map(s -> new AppUserRole("ROLE_"+s))
                                .collect(Collectors.toSet());
        log.debug("Roles: {}", roles);
        user.setRoles(roles);
        var response = userRepository.save(user);
        return ResponseEntity.ok(response);
    }

    // POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<Authentication> login(@RequestBody LoginRequest request) {
        log.debug("Login request: {}", request);
        // Authentiction object that needs to be authenticated
        Authentication authRequest = unauthenticated(request.userName(), request.password());

        // Authentication object after authentication
        Authentication authResult = authenticationManager.authenticate(authRequest);
        log.debug("Auth result: {}", authResult);

        SecurityContextHolder.getContext().setAuthentication(authResult);
        return ResponseEntity.ok(authResult);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Error: ", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
