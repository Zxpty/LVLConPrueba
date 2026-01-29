package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class FindProjectsRequestDto(
    @SerializedName("start")
    val start: Int? = null,
    @SerializedName("sort")
    val sort: String? = null,
    @SerializedName("limit")
    val limit: Int? = null,
    @SerializedName("categoriaId")
    val categoriaId: String? = null,
    @SerializedName("estadoCodigo")
    val estadoCodigo: String? = null,
    @SerializedName("iconoCodigo")
    val iconoCodigo: String? = null,
    @SerializedName("codigo")
    val codigo: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,
    @SerializedName("fechaFin")
    val fechaFin: String? = null
)
