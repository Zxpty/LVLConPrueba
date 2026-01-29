package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class ProjectInitFormResponseDto(
    @SerializedName("proyectoId")
    val proyectoId: String? = null,
    @SerializedName("categoriaId")
    val categoriaId: String? = null,
    @SerializedName("usuarioId")
    val usuarioId: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("descripcion")
    val descripcion: String? = null,
    @SerializedName("estadoCodigo")
    val estadoCodigo: String? = null,
    @SerializedName("iconoCodigo")
    val iconoCodigo: String? = null,
    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,
    @SerializedName("fechaFin")
    val fechaFin: String? = null,
    @SerializedName("compartir")
    val compartir: Boolean? = null,
    @SerializedName("combo")
    val combo: ComboDto? = null
)

data class ComboDto(
    @SerializedName("estado")
    val estado: ComboListDto<EstadoDto>? = null,
    @SerializedName("icono")
    val icono: ComboListDto<IconoDto>? = null,
    @SerializedName("categoria")
    val categoria: ComboListDto<CategoriaDto>? = null
)

data class ComboListDto<T>(
    @SerializedName("list")
    val list: List<T>? = null,
    @SerializedName("size")
    val size: Int? = null
)

data class EstadoDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("url")
    val url: String? = null
)

data class IconoDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("url")
    val url: String? = null
)

data class CategoriaDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("url")
    val url: String? = null
)
