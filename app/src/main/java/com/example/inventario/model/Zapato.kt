package com.example.inventario.model

import kotlinx.serialization.Serializable

@Serializable
data class Zapato(
    val id: Int? = null,
    val estilo: String,
    val precio: Double,
    val descripcion: String,
    val imagen: String,
    val tallasCantidades: String
)