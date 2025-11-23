package com.example.inventario.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "inventario") {
        composable("inventario") {
            // InventarioScreen() // This will be updated later
        }
        composable("agregar_zapato") {
            // AgregarZapatoScreen() // This will be created next
        }
    }
}