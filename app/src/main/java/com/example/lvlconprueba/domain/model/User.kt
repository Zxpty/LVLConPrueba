package com.example.lvlconprueba.domain.model

data class User(
    val usuarioId: String = "",
    val rolId: String = "",
    val usuario: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val password: String = "",
    val empresa: String = "",
    val cargoCodigo: String = "",
    val correo: String = "",
    val telefono: String = "",
    val url: String = "",
    val token: String? = null,
    val rolCodigo: String? = null,
    val rol: String? = null
)
