package ru.nsu.brusn.smpltodo.service.folder;

import org.springframework.stereotype.Service;
import ru.nsu.brusn.smpltodo.exception.authentication.NoAccessToResourceException;
import ru.nsu.brusn.smpltodo.exception.folder.FolderAlreadyExistsException;
import ru.nsu.brusn.smpltodo.exception.folder.FolderNotFoundException;
import ru.nsu.brusn.smpltodo.mapper.FolderMapper;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewFolderRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.ModifyFolderAttributesRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.UserFolderResponse;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.FolderRepository;
import ru.nsu.brusn.smpltodo.repository.TaskRepository;
import ru.nsu.brusn.smpltodo.security.access.AccessChecker;
import ru.nsu.brusn.smpltodo.util.StringHandler;
import ru.nsu.brusn.smpltodo.util.validator.FolderNameValidator;

import java.util.ArrayList;

@Service
public class FolderService {
    private final FolderMapper folderMapper;
    private final TaskRepository taskRepository;
    private final FolderRepository folderRepository;
    private final FolderNameValidator folderNameValidator;

    public FolderService(FolderMapper folderMapper, TaskRepository taskRepository, FolderRepository folderRepository, FolderNameValidator folderNameValidator) {
        this.folderMapper = folderMapper;
        this.taskRepository = taskRepository;
        this.folderRepository = folderRepository;
        this.folderNameValidator = folderNameValidator;
    }

    public ResponseWrapper<Object> createNewFolder(UserEntity user, CreateNewFolderRequest request) throws FolderAlreadyExistsException {
        var folderEntity = new FolderEntity();

        var folderName = request.getName();
        folderNameValidator.validateData(folderName);
        folderEntity.setName(folderName);
        folderEntity.setUser(user);

        folderRepository.save(folderEntity);
        return ResponseWrapper.okResponse("Folder created successfully");
    }

    public ResponseWrapper<Object> getAllUserFolders(UserEntity user) {
        var userFolders = new ArrayList<UserFolderResponse>();
        user.getFolders().forEach(e -> {
            userFolders.add(folderMapper.getMapped(e));
        });
        return new ResponseWrapper<>(userFolders);
    }

    public ResponseWrapper<Object> getAllFolderTasks(UserEntity user, Long folderId) {
        var folder = folderRepository.findFolderEntityById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not found", TError.BAD_REQUEST));
        if (!AccessChecker.hasAccessToFolder(user, folder)) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }
        return new ResponseWrapper<>(taskRepository.findTaskEntityByFolder(folder));
    }

    public ResponseWrapper<Object> modifyFolderAttributes(UserEntity user, Long folderId, ModifyFolderAttributesRequest request) {
        var folder = folderRepository.findFolderEntityById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));

        if (!AccessChecker.hasAccessToFolder(user, folder)) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }

        var folderName = request.getName();
        if (!StringHandler.isStringEmptyOrNull(folderName)) {
            folderNameValidator.validateData(folderName);
            folder.setName(folderName);
        }

        folderRepository.save(folder);
        return ResponseWrapper.okResponse("Folder attributes changed successfully");
    }

    public ResponseWrapper<Object> deleteFolder(UserEntity user, Long folderId) {
        var folderEntity = folderRepository.findFolderEntityById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));

        if (!AccessChecker.hasAccessToFolder(user, folderEntity)) {
            throw new NoAccessToResourceException(TError.NO_ACCESS, "Authenticated user don't have access to this folder");
        }

        folderRepository.delete(folderEntity);
        return ResponseWrapper.okResponse("Folder deleted successfully");
    }
}
