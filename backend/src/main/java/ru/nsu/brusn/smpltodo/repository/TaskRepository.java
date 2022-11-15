package ru.nsu.brusn.smpltodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;
import ru.nsu.brusn.smpltodo.model.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findTaskEntityById(Long id);
    Optional<List<TaskEntity>> findTaskEntityByFolder(FolderEntity folder);
}
