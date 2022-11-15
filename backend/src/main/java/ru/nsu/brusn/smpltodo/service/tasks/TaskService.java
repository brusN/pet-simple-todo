package ru.nsu.brusn.smpltodo.service.tasks;

import org.springframework.stereotype.Service;
import ru.nsu.brusn.smpltodo.exception.authentication.NoAccessToResourceException;
import ru.nsu.brusn.smpltodo.exception.folder.FolderNotFoundException;
import ru.nsu.brusn.smpltodo.exception.task.TaskNotFoundException;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewTaskRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.ModifyTaskAttributesRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.TaskEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.FolderRepository;
import ru.nsu.brusn.smpltodo.repository.TaskRepository;
import ru.nsu.brusn.smpltodo.security.access.AccessChecker;
import ru.nsu.brusn.smpltodo.util.StringHandler;
import ru.nsu.brusn.smpltodo.util.validator.TaskNameValidator;

import java.time.ZonedDateTime;

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

    private void setNewTaskNoRequiredPropertiesIfNotNull(TaskEntity newTask, CreateNewTaskRequest request) {
        if (request.getStartDate() != null) {
            newTask.setStartDate(request.getStartDate());
        }

        if (request.getDeadline() != null) {
            newTask.setDeadline(request.getDeadline());
        }

        if (request.getDescription() != null) {
            newTask.setDescription(request.getDescription());
        }
    }

    private void updateTaskAttributesIfNotNull(UserEntity user, TaskEntity task, ModifyTaskAttributesRequest request) {
        var newName = request.getName();
        if (!StringHandler.isStringEmptyOrNull(newName)) {
            taskNameValidator.validateData(newName);
            task.setName(newName);
        }

        if (request.getStartDate() != null) {
            task.setStartDate(request.getStartDate());
        }

        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }

        var newDescription = request.getDescription();
        if (!StringHandler.isStringEmptyOrNull(newDescription)) {
            task.setDescription(newDescription);
        }

        if (request.getImportant() != null) {
            task.setImportant(request.getImportant());
        }

        if (request.getCompleted() != null) {
            task.setImportant(request.getCompleted());
        }

        var newFolderId = request.getNewFolderId();
        if (newFolderId != null) {
            var newFolder = folderRepository.findFolderEntityById(newFolderId)
                    .orElseThrow(() -> new FolderNotFoundException("New folder not exists", TError.BAD_REQUEST));
            if (!AccessChecker.hasAccessToFolder(user, newFolder)) {
                throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
            }
            task.setFolder(newFolder);
        }
    }

    public ResponseWrapper<Object> createNewTask(UserEntity user, CreateNewTaskRequest request) {
        var folder = folderRepository.findFolderEntityById(request.getFolderId())
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));
        if (!AccessChecker.hasAccessToFolder(user, folder)) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }

        var newTask = new TaskEntity();
        var taskName = request.getName();
        taskNameValidator.validateData(taskName);
        newTask.setName(taskName);
        setNewTaskNoRequiredPropertiesIfNotNull(newTask, request);
        newTask.setFolder(folder);

        newTask.setImportant(false);
        newTask.setCompleted(false);
        newTask.setCreated(ZonedDateTime.now());
        taskRepository.save(newTask);

        return ResponseWrapper.okResponse("Task created successfully");
    }

    public ResponseWrapper<Object> modifyTaskAttributes(UserEntity user, Long taskId, ModifyTaskAttributesRequest request) {
        var task = taskRepository.findTaskEntityById(taskId).orElseThrow(
                () -> new TaskNotFoundException("Task not found", TError.BAD_REQUEST));
        if (!AccessChecker.hasAccessToFolder(user, task.getFolder())) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }

        updateTaskAttributesIfNotNull(user, task, request);

        taskRepository.save(task);
        return ResponseWrapper.okResponse("Task attributes updated successfully");
    }

    public ResponseWrapper<Object> deleteTask(UserEntity user, Long taskId) {
        var task = taskRepository.findTaskEntityById(taskId).orElseThrow(
                () -> new TaskNotFoundException("Task not found", TError.BAD_REQUEST));
        if (!AccessChecker.hasAccessToFolder(user, task.getFolder())) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }
        taskRepository.delete(task);
        return ResponseWrapper.okResponse("Task deleted successfully");
    }
}
