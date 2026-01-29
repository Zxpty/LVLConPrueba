package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("usuario")
    val usuario: String,
    @SerializedName("password")
    val password: String
)
