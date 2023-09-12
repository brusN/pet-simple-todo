package ru.nsu.brusn.smpltodo.api.model.dto.response

class MessageResponse (
    val error: Error,
    val data: Data,
) {
    data class Error (
        val errorType: String,
        val message: String,
    )

    data class Data (
        val message: String,
    )
}