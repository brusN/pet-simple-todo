package ru.nsu.brusn.smpltodo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.brusn.smpltodo.exception.auth.UserNotFoundException;
import ru.nsu.brusn.smpltodo.model.dto.request.SignInRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.SignUpRequest;
import ru.nsu.brusn.smpltodo.service.auth.AuthService;

import javax.management.relation.RoleNotFoundException;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request) throws AuthenticationException, UserNotFoundException {
        var response = authService.signin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) throws RoleNotFoundException {
        var response = authService.signup(request);
        return ResponseEntity.ok(response);
    }
}
