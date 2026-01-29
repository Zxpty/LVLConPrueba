package com.example.lvlconprueba.data.mapper

import com.example.lvlconprueba.data.dto.*
import com.example.lvlconprueba.domain.model.*

fun ProjectInitFormResponseDto.toDomain(): Project {
    return Project(
        proyectoId = proyectoId,
        categoriaId = categoriaId,
        usuarioId = usuarioId,
        codigo = codigo,
        nombre = nombre,
        descripcion = descripcion,
        estadoCodigo = estadoCodigo,
        iconoCodigo = iconoCodigo,
        fechaInicio = fechaInicio,
        fechaFin = fechaFin,
        compartir = compartir
    )
}

fun ProjectResponseDto.toDomain(): Project {
    return Project(
        proyectoId = proyectoId,
        categoriaId = categoriaId,
        categoriaNombre = null,
        usuarioId = usuarioId,
        codigo = codigo,
        nombre = nombre,
        descripcion = descripcion,
        estadoCodigo = estadoCodigo,
        estadoNombre = estadoNombre,
        iconoCodigo = iconoCodigo,
        iconoNombre = iconoNombre,
        fechaInicio = fechaInicio,
        fechaFin = fechaFin,
        compartir = compartir
    )
}

fun EstadoDto.toDomain(): Estado {
    return Estado(
        id = id,
        codigo = codigo,
        nombre = nombre
    )
}

fun IconoDto.toDomain(): Icono {
    return Icono(
        id = id,
        codigo = codigo,
        nombre = nombre
    )
}

fun CategoriaDto.toDomain(): Categoria {
    return Categoria(
        id = id,
        nombre = nombre
    )
}

fun ProjectInitFormResponseDto.toComboDomain(): ProjectComboData {
    return ProjectComboData(
        estados = combo?.estado?.list?.map { it.toDomain() } ?: emptyList(),
        iconos = combo?.icono?.list?.map { it.toDomain() } ?: emptyList(),
        categorias = combo?.categoria?.list?.map { it.toDomain() } ?: emptyList()
    )
}

fun FindProjectsResponseDto.toDomain(): List<Project> {
    return elements?.mapNotNull { it.toDomain() } ?: emptyList()
}

fun LoginResponseDto.toDomain(): User {
    return User(
        token = token,
        usuarioId = usuarioId ?: "",
        nombre = nombre ?: "",
        usuario = usuario ?: "",
        rolCodigo = rolCodigo ?: "",
        rol = rol ?: ""
    )
}
