package ru.nsu.brusn.smpltodo.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.brusn.smpltodo.exception.auth.UserAlreadyExists;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;
import ru.nsu.brusn.smpltodo.model.dto.request.SignInRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.SignUpRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.SignInResponse;
import ru.nsu.brusn.smpltodo.model.entity.ERole;
import ru.nsu.brusn.smpltodo.model.entity.RoleEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.RoleRepository;
import ru.nsu.brusn.smpltodo.repository.UserRepository;
import ru.nsu.brusn.smpltodo.service.TokenService;
import ru.nsu.brusn.smpltodo.util.validator.PasswordValidator;
import ru.nsu.brusn.smpltodo.util.validator.UsernameValidator;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;

    private Set<RoleEntity> getDefaultUserRoles() throws RoleNotFoundException {
        var userRole = roleRepository.findByRole(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFoundException("User role not exists!"));
        return Set.of(userRole);
    }

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UsernameValidator usernameValidator, PasswordValidator passwordValidator) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
    }

    public SignInResponse signin(SignInRequest request) throws AuthenticationException {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        return new SignInResponse(tokenService.generateToken(authentication));
    }

    public void signup(SignUpRequest request) throws UserAlreadyExists, RoleNotFoundException, DataValidationException {
        // Unique username validation
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExists("User with username " + request.getUsername() + "already exists!");
        }

        // Validating by patterns
        usernameValidator.validateData(request.getUsername());
        passwordValidator.validateData(request.getPassword());

        // Creating new user and save in DB
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRoles(getDefaultUserRoles());
        userRepository.save(userEntity);
    }
}
