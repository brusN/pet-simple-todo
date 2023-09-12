package ru.nsu.brusn.smpltodo.api.model.dto.response

import ru.nsu.brusn.smpltodo.api.model.task.TaskEntity

data class GetFolderTasksResponse(
    val data: List<TaskEntity>
)
