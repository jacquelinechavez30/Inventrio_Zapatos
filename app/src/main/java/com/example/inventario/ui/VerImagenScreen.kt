package com.example.inventario.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.inventario.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerImagenScreen(navController: NavController, imageUrl: String?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imagen del Zapato") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del zapato en pantalla completa",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentScale = ContentScale.Fit
        )
    }
}