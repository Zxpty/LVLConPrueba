package com.example.lvlconprueba.data.dto

import com.google.gson.annotations.SerializedName

data class FindProjectsResponseDto(
    @SerializedName("elements")
    val elements: List<ProjectResponseDto>? = null,
    @SerializedName("start")
    val start: Int? = null,
    @SerializedName("sort")
    val sort: String? = null,
    @SerializedName("limit")
    val limit: Int? = null,
    @SerializedName("totalCount")
    val totalCount: Int? = null
)
