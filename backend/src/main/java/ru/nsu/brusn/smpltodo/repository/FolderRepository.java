package ru.nsu.brusn.smpltodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.brusn.smpltodo.model.entity.TaskFolderEntity;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<TaskFolderEntity, Long> {
    Optional<TaskFolderEntity> findTaskFolderEntityByName(String name);
}
