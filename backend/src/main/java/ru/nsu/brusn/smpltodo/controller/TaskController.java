package ru.nsu.brusn.smpltodo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.brusn.smpltodo.model.dto.request.CreateNewTaskRequest;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;
import ru.nsu.brusn.smpltodo.service.tasks.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Object> createNewTask(@AuthenticationPrincipal UserEntity user, @RequestBody CreateNewTaskRequest request) {
        var response = taskService.createNewTask(user, request);
        return ResponseEntity.ok(response);
    }
}
