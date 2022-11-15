package ru.nsu.brusn.smpltodo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewTaskRequest;
import ru.nsu.brusn.smpltodo.model.dto.request.ModifyTaskAttributesRequest;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.service.tasks.TaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewTask(@AuthenticationPrincipal UserEntity user, @RequestBody CreateNewTaskRequest request) {
        var response = taskService.createNewTask(user, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<Object> modifyTaskAttributes(@AuthenticationPrincipal UserEntity user, @PathVariable(value = "id")Long taskId, @RequestBody ModifyTaskAttributesRequest request) {
        var response = taskService.modifyTaskAttributes(user, taskId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTask(@AuthenticationPrincipal UserEntity user, @PathVariable(value = "id") Long taskId) {
        var response = taskService.deleteTask(user, taskId);
        return ResponseEntity.ok(response);
    }
}
