package ru.nsu.brusn.smpltodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.brusn.smpltodo.model.entity.ERole;
import ru.nsu.brusn.smpltodo.model.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(ERole role);
    boolean existsByRole(ERole role);
}
