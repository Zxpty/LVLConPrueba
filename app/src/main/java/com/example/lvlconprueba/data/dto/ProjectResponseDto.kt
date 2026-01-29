package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class ProjectResponseDto(
    @SerializedName("proyectoId")
    val proyectoId: String? = null,
    @SerializedName("categoriaId")
    val categoriaId: String? = null,
    @SerializedName("usuarioId")
    val usuarioId: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("descripcion")
    val descripcion: String? = null,
    @SerializedName("estadoCodigo")
    val estadoCodigo: String? = null,
    @SerializedName("estado")
    val estadoNombre: String? = null,
    @SerializedName("iconoCodigo")
    val iconoCodigo: String? = null,
    @SerializedName("icono")
    val iconoNombre: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,
    @SerializedName("fechaFin")
    val fechaFin: String? = null,
    @SerializedName("compartir")
    val compartir: Boolean? = null
)
