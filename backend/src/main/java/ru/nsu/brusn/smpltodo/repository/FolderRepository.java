package ru.nsu.brusn.smpltodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    Optional<FolderEntity> findFolderEntityById(Long id);
}
