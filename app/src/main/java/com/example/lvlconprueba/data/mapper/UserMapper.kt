package com.example.lvlconprueba.data.mapper

import com.example.lvlconprueba.data.dto.UserDto
import com.example.lvlconprueba.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        usuarioId = usuarioId ?: "",
        nombre = nombre ?: "",
        apellido = apellido ?: "",
        cargoCodigo = cargoCodigo ?: "",
        telefono = telefono ?: "",
        correo = correo ?: ""
    )
}
