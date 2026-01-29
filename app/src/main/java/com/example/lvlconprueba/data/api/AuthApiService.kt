package com.example.lvlconprueba.data.api

import com.example.lvlconprueba.data.dto.LoginRequestDto
import com.example.lvlconprueba.data.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>
}
