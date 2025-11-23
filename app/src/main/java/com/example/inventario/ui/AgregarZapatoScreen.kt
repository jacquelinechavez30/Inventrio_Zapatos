package com.example.inventario.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.inventario.R
import com.example.inventario.model.Zapato
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarZapatoScreen(navController: NavController, viewModel: ZapatosViewModel) {
    var estilo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tallasCantidades by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Zapato") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Atrás")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = estilo,
                onValueChange = { estilo = it },
                label = { Text("Estilo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = tallasCantidades,
                onValueChange = { tallasCantidades = it },
                label = { Text("Tallas y Cantidades") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (imagenUri != null) {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        var imageUrl = ""
                        imagenUri?.let {
                            val imageName = "${UUID.randomUUID()}"
                            val imageBytes = context.contentResolver.openInputStream(it)?.readBytes()
                            if (imageBytes != null) {
                                imageUrl = viewModel.subirImagen(imageBytes, imageName)
                            }
                        }

                        val precioDouble = precio.toDoubleOrNull() ?: 0.0

                        val nuevoZapato = Zapato(
                            estilo = estilo,
                            precio = precioDouble,
                            descripcion = descripcion,
                            imagen = imageUrl,
                            tallasCantidades = tallasCantidades
                        )
                        viewModel.agregarZapato(nuevoZapato)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}