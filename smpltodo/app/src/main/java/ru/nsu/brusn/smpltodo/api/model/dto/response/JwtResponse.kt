package ru.nsu.brusn.smpltodo.api.model.dto.response

data class JwtResponse(
    val error: Error,
    val data: Data,
) {
    data class Error (
        val errorType: String,
        val message: String,
    )

    data class Data (
        val jwt: String,
    )
}