package ru.nsu.brusn.smpltodo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.brusn.smpltodo.exception.auth.UserAlreadyExists;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;
import ru.nsu.brusn.smpltodo.model.dto.request.SignInRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.SignUpRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.SignInResponse;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.ERole;
import ru.nsu.brusn.smpltodo.model.entity.RoleEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.RoleRepository;
import ru.nsu.brusn.smpltodo.repository.UserRepository;
import ru.nsu.brusn.smpltodo.service.TokenService;
import ru.nsu.brusn.smpltodo.service.auth.AuthService;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request) throws AuthenticationException {
        try {
            var response = new ResponseWrapper<>(authService.signin(request));
            response.setMessage("Sign in success");
            return ResponseEntity.ok(new ResponseWrapper<>(response));
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.errorResponse(TError.BAD_REQUEST.name(), e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            authService.signup(request);
            return ResponseEntity.ok(ResponseWrapper.okResponse("Sign up success"));
        } catch (RoleNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.errorResponse(TError.API_ERROR.name(), "Error while creating new user"));
        } catch (UserAlreadyExists e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.errorResponse(TError.BAD_REQUEST.name(), "Username already occupied"));
        } catch (DataValidationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.errorResponse(TError.BAD_REQUEST.name(), e.getMessage()));
        }
    }
}
