package ru.nsu.brusn.smpltodo.service.folder;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.brusn.smpltodo.exception.folder.FolderAlreadyExistsException;
import ru.nsu.brusn.smpltodo.exception.folder.FolderNotFoundException;
import ru.nsu.brusn.smpltodo.model.dto.request.ModifyFolderAttributesRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewFolderRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.repository.FolderRepository;
import ru.nsu.brusn.smpltodo.util.StringHandler;
import ru.nsu.brusn.smpltodo.util.validator.FolderNameValidator;

import java.util.Objects;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderNameValidator folderNameValidator;

    private boolean hasAccessToFolder(FolderEntity folder, UserEntity user) {
        return Objects.equals(folder.getUser().getId(), user.getId());
    }

    public FolderService(FolderRepository folderRepository, FolderNameValidator folderNameValidator) {
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
        return new ResponseWrapper<>(user.getFolders());
    }

    public ResponseWrapper<Object> modifyFolderAttributes(UserEntity user, Long folderId, ModifyFolderAttributesRequest request) {
        var folderEntity = folderRepository.findFolderEntityById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));

        // Verify authenticated user owns this folder
        if (!hasAccessToFolder(folderEntity, user)) {
            return ResponseWrapper.errorResponse(TError.BAD_REQUEST, "Authenticated user don't have access to this folder");
        }

        var folderName = request.getName();
        if (StringHandler.isStringEmptyOrNull(folderName)) {
            return ResponseWrapper.errorResponse(TError.BAD_REQUEST, "Invalid folder name");
        }
        folderNameValidator.validateData(folderName);
        folderEntity.setName(folderName);
        folderRepository.save(folderEntity);
        return ResponseWrapper.okResponse("Folder attributes changed successfully");
    }

    public ResponseWrapper<Object> deleteFolder(UserEntity user, Long folderId) {
        var folderEntity = folderRepository.findFolderEntityById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not exists", TError.BAD_REQUEST));

        if (!hasAccessToFolder(folderEntity, user)) {
            return ResponseWrapper.errorResponse(TError.BAD_REQUEST, "Authenticated user don't have access to this folder");
        }

        folderRepository.delete(folderEntity);
        return ResponseWrapper.okResponse("Folder deleted successfully");
    }
}
