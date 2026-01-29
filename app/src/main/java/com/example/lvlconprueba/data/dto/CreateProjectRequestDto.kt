package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class CreateProjectRequestDto(
    @SerializedName("proyectoId")
    val proyectoId: String? = null,
    @SerializedName("categoriaId")
    val categoriaId: String?,
    @SerializedName("usuarioId")
    val usuarioId: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("nombre")
    val nombre: String?,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("estadoCodigo")
    val estadoCodigo: String?,
    @SerializedName("iconoCodigo")
    val iconoCodigo: String?,
    @SerializedName("fechaInicio")
    val fechaInicio: String?,
    @SerializedName("fechaFin")
    val fechaFin: String?,
    @SerializedName("compartir")
    val compartir: Boolean = false
)

data class CreateProjectResponseDto(
    @SerializedName("codigo")
    val codigo: String?,
    @SerializedName("mensaje")
    val mensaje: String?
)
