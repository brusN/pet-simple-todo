package ru.nsu.brusn.smpltodo.api.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.nsu.brusn.smpltodo.api.model.dto.request.SignInRequest
import ru.nsu.brusn.smpltodo.api.model.dto.request.SignUpRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.JwtResponse
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse

interface AuthService {
    @POST("/api/auth/signin")
    fun signIn(@Body request: SignInRequest): Call<JwtResponse>

    @POST("/api/auth/signup")
    fun signUp(@Body request: SignUpRequest): Call<MessageResponse>
}