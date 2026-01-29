package com.example.lvlconprueba.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    val proyectoId: String? = null,
    val categoriaId: String? = null,
    val categoriaNombre: String? = null,
    val usuarioId: String? = null,
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val estadoCodigo: String? = null,
    val estadoNombre: String? = null,
    val iconoCodigo: String? = null,
    val iconoNombre: String? = null,
    val fechaInicio: String? = null,
    val fechaFin: String? = null,
    val compartir: Boolean? = null
) : Parcelable

@Parcelize
data class Estado(
    val id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null
) : Parcelable

@Parcelize
data class Icono(
    val id: String? = null,
    val codigo: String? = null,
    val nombre: String? = null
) : Parcelable

@Parcelize
data class Categoria(
    val id: String? = null,
    val nombre: String? = null
) : Parcelable

@Parcelize
data class ProjectComboData(
    val estados: List<Estado> = emptyList(),
    val iconos: List<Icono> = emptyList(),
    val categorias: List<Categoria> = emptyList()
) : Parcelable
