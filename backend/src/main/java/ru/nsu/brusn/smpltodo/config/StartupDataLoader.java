package ru.nsu.brusn.smpltodo.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nsu.brusn.smpltodo.exception.other.RoleNotFoundException;
import ru.nsu.brusn.smpltodo.model.entity.RoleEntity;
import ru.nsu.brusn.smpltodo.repository.RoleRepository;
import ru.nsu.brusn.smpltodo.model.entity.ERole;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.UserRepository;

import java.util.Set;

@Component
public class StartupDataLoader implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public StartupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creating users roles if not exists
        if (!roleRepository.existsByRole(ERole.ROLE_USER)) {
            roleRepository.save(new RoleEntity(ERole.ROLE_USER));
        }
        if (!roleRepository.existsByRole(ERole.ROLE_ADMIN)) {
            roleRepository.save(new RoleEntity(ERole.ROLE_ADMIN));
        }

        // Creating root admin if not exist
        if (!userRepository.existsByUsername("root")) {
            var rootAdminEntity = new UserEntity();
            rootAdminEntity.setUsername("root");
            rootAdminEntity.setPassword(passwordEncoder.encode("rootpassword"));
            var userRoleEntity = roleRepository.findByRole(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFoundException("User role not exists!"));
            var adminRoleEntity = roleRepository.findByRole(ERole.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException("Admin role not exists!"));
            rootAdminEntity.setRoles(Set.of(userRoleEntity, adminRoleEntity));
            userRepository.save(rootAdminEntity);
        }
    }
}
