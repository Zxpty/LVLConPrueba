package com.example.lvlconprueba.data.api

import com.example.lvlconprueba.data.dto.UpdateUserRequestDto
import com.example.lvlconprueba.data.dto.UpdateUserResponseDto
import com.example.lvlconprueba.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @GET("api/usuario/get/{usuarioId}")
    suspend fun getUserById(@Path("usuarioId") usuarioId: String): Response<UserDto>

    @POST("api/usuario/saveOrUpdate")
    suspend fun saveOrUpdateUser(@Body request: UpdateUserRequestDto): Response<UpdateUserResponseDto>
}
