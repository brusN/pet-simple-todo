package ru.nsu.brusn.smpltodo.api.model.dto.request

import com.squareup.moshi.Json

data class CreateNewFolderRequest(
    @field:Json(name = "name")
    val name: String,
)