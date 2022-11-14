package ru.nsu.brusn.smpltodo.service.tasks;

import org.springframework.stereotype.Service;
import ru.nsu.brusn.smpltodo.exception.folder.FolderNotFoundException;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewTaskRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;
import ru.nsu.brusn.smpltodo.model.entity.TaskEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.FolderRepository;
import ru.nsu.brusn.smpltodo.repository.TaskRepository;
import ru.nsu.brusn.smpltodo.util.validator.TaskNameValidator;

import java.util.Objects;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final FolderRepository folderRepository;
    private final TaskNameValidator taskNameValidator;

    public TaskService(TaskRepository taskRepository, FolderRepository folderRepository, TaskNameValidator taskNameValidator) {
        this.taskRepository = taskRepository;
        this.folderRepository = folderRepository;
        this.taskNameValidator = taskNameValidator;
    }

    private boolean hasAccessToFolder(FolderEntity folder, UserEntity user) {
        return Objects.equals(folder.getUser().getId(), user.getId());
    }

    public ResponseWrapper<Object> createNewTask(UserEntity user, CreateNewTaskRequest request) {
        var folder = folderRepository.findFolderEntityById(request.getFolderId())
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));
        if (!hasAccessToFolder(folder, user)) {
            return ResponseWrapper.errorResponse(TError.BAD_REQUEST, "No access to folder");
        }



        var newTask = new TaskEntity();
        newTask.setName(request.getName());
        return ResponseWrapper.okResponse("ok");
    }
}
