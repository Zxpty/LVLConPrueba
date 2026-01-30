package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("apellido") val apellido: String? = null,
    @SerializedName("cargoCodigo") val cargoCodigo: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("usuarioId") val usuarioId: String? = null
)

data class UpdateUserRequestDto(
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("telefono") val telefono: String,
)

data class UpdateUserResponseDto(
    @SerializedName("codigo") val codigo: String?,
    @SerializedName("mensaje") val mensaje: String?
)
