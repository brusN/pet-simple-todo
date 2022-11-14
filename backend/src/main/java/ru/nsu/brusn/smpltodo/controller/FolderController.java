package ru.nsu.brusn.smpltodo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nsu.brusn.smpltodo.model.dto.request.ModifyFolderAttributesRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewFolderRequest;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.service.folder.FolderService;

@Slf4j
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewFolder(@AuthenticationPrincipal UserEntity user, @RequestBody CreateNewFolderRequest request) {
        var response = folderService.createNewFolder(user, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUserFolders(@AuthenticationPrincipal UserEntity user) {
        var response = folderService.getAllUserFolders(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<Object> modifyFolderAttributes(@AuthenticationPrincipal UserEntity user, @PathVariable(value = "id") Long folderId, @RequestBody ModifyFolderAttributesRequest request) {
        var response = folderService.modifyFolderAttributes(user, folderId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteFolder(@AuthenticationPrincipal UserEntity user, @PathVariable(value = "id") Long folderId) {
        var response = folderService.deleteFolder(user, folderId);
        return ResponseEntity.ok(response);
    }
}
