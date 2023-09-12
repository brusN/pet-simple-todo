package ru.nsu.brusn.smpltodo.api.model.dto.request

import com.squareup.moshi.Json

data class SignInRequest(
    @field:Json(name = "username")
    val username: String,

    @field:Json(name = "password")
    val password: String
)