package ru.nsu.brusn.smpltodo.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.brusn.smpltodo.exception.auth.UserAlreadyExistsException;
import ru.nsu.brusn.smpltodo.exception.auth.UserNotFoundException;
import ru.nsu.brusn.smpltodo.exception.validation.DataValidationException;
import ru.nsu.brusn.smpltodo.model.dto.request.SignInRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.SignUpRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.SignInResponse;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.ERole;
import ru.nsu.brusn.smpltodo.model.entity.RoleEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.RoleRepository;
import ru.nsu.brusn.smpltodo.repository.UserRepository;
import ru.nsu.brusn.smpltodo.security.jwt.JwtUtils;
import ru.nsu.brusn.smpltodo.util.validator.PasswordValidator;
import ru.nsu.brusn.smpltodo.util.validator.UsernameValidator;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final JwtUtils jwtUtils;

    private Set<RoleEntity> getDefaultUserRoles() throws RoleNotFoundException {
        var userRole = roleRepository.findByRole(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFoundException("Role not exists!"));
        return Set.of(userRole);
    }

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, UsernameValidator usernameValidator, PasswordValidator passwordValidator) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
        this.jwtUtils = jwtUtils;
    }

    public SignInResponse signin(SignInRequest request) throws AuthenticationException, UserNotFoundException {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User doesn't exists", TError.BAD_REQUEST));
        String token = jwtUtils.generateToken(user);
        return new SignInResponse(token);
    }

    public void signup(SignUpRequest request) throws UserAlreadyExistsException, RoleNotFoundException, DataValidationException {
        // Unique username validation
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + "already exists!", TError.BAD_REQUEST);
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
