package com.example.inventario.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun EditarZapatoScreen(navController: NavController, viewModel: ZapatosViewModel, zapatoId: Int) {
    val zapato by viewModel.selectedZapato.collectAsState()

    var estilo by remember(zapato) { mutableStateOf(zapato?.estilo ?: "") }
    var precio by remember(zapato) { mutableStateOf(zapato?.precio?.toString() ?: "") }
    var cantidad by remember(zapato) { mutableStateOf(zapato?.cantidad?.toString() ?: "") }
    var descripcion by remember(zapato) { mutableStateOf(zapato?.descripcion ?: "") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

    LaunchedEffect(zapatoId) {
        viewModel.getZapatoById(zapatoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Zapato") },
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
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                    Text("Cambiar Imagen")
                }
                Spacer(modifier = Modifier.width(16.dp))
                AsyncImage(
                    model = imagenUri ?: zapato?.imagen,
                    contentDescription = "Imagen del zapato",
                    modifier = Modifier.size(100.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        var imageUrl = zapato?.imagen ?: ""
                        imagenUri?.let { uri ->
                            val imageName = "${UUID.randomUUID()}"
                            val imageBytes = context.contentResolver.openInputStream(uri)?.readBytes()
                            if (imageBytes != null) {
                                imageUrl = viewModel.subirImagen(imageBytes, imageName)
                            }
                        }

                        val precioDouble = precio.toDoubleOrNull() ?: zapato?.precio ?: 0.0
                        val cantidadInt = cantidad.toIntOrNull() ?: zapato?.cantidad ?: 0

                        val zapatoActualizado = zapato?.copy(
                            estilo = estilo,
                            precio = precioDouble,
                            cantidad = cantidadInt,
                            descripcion = descripcion,
                            imagen = imageUrl
                        )
                        if (zapatoActualizado != null) {
                            viewModel.actualizarZapato(zapatoActualizado)
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}