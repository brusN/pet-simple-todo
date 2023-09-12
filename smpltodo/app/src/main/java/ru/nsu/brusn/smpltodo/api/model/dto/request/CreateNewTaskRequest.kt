package ru.nsu.brusn.smpltodo.api.model.dto.request

import com.squareup.moshi.Json

data class CreateNewTaskRequest(
    @field:Json(name = "folderId")
    val folderId: Long,

    @field:Json(name = "name")
    val name: String,
)