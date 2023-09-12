package ru.nsu.brusn.smpltodo.api.model.dto.request

import com.squareup.moshi.Json

data class EditFolderRequest (
    @field:Json(name = "name")
    val name: String,
)