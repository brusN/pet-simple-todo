package ru.nsu.brusn.smpltodo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateFolderRequest;
import ru.nsu.brusn.smpltodo.model.dto.response.common.ResponseWrapper;
import ru.nsu.brusn.smpltodo.model.dto.response.common.TError;
import ru.nsu.brusn.smpltodo.model.entity.TaskFolderEntity;
import ru.nsu.brusn.smpltodo.repository.FolderRepository;

@Slf4j
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderRepository folderRepository;

    public FolderController(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewFolder(@RequestBody CreateFolderRequest request) {
        var folder = folderRepository.findTaskFolderEntityByName(request.getName());
        if (folder.isPresent()) {
            return ResponseEntity.badRequest().body(ResponseWrapper.errorResponse(TError.BAD_REQUEST, "Folder already exists"));
        }
        TaskFolderEntity folderEntity = new TaskFolderEntity();
        folderEntity.setName(request.getName());
        folderRepository.save(folderEntity);
        return ResponseEntity.ok(ResponseWrapper.okResponse("Folder created"));
    }
}
