package ru.nsu.brusn.smpltodo.api.model.dto.response

import ru.nsu.brusn.smpltodo.api.model.folder.FolderEntity

data class GetAllFoldersResponse(
    val data: List<FolderEntity>
)