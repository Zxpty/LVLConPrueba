package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("usuario")
    val usuario: String? = null,
    @SerializedName("usuarioId")
    val usuarioId: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("rolCodigo")
    val rolCodigo: String? = null,
    @SerializedName("rol")
    val rol: String? = null,
    @SerializedName("exito")
    val exito: Boolean? = null,
    @SerializedName("mensaje")
    val mensaje: String? = null
)
