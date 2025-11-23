package com.example.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.inventario.model.Zapato
import com.example.inventario.ui.AgregarZapatoScreen
import com.example.inventario.ui.EditarZapatoScreen
import com.example.inventario.ui.ZapatosViewModel
import com.example.inventario.ui.theme.InventarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventarioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InventarioApp()
                }
            }
        }
    }
}

@Composable
fun InventarioApp() {
    val navController = rememberNavController()
    val viewModel: ZapatosViewModel = viewModel()
    NavHost(navController = navController, startDestination = "inventario") {
        composable("inventario") {
            InventarioScreen(navController = navController, viewModel = viewModel)
        }
        composable("agregar_zapato") {
            AgregarZapatoScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            "editar_zapato/{zapatoId}",
            arguments = listOf(navArgument("zapatoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val zapatoId = backStackEntry.arguments?.getInt("zapatoId")
            if (zapatoId != null) {
                EditarZapatoScreen(
                    navController = navController,
                    viewModel = viewModel,
                    zapatoId = zapatoId
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(navController: NavController, viewModel: ZapatosViewModel) {
    val zapatos by viewModel.zapatos.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario de Zapatos") },
                actions = {
                    IconButton(onClick = { viewModel.obtenerZapatos() }) { 
                        Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = "Refrescar")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Agregar") },
                            onClick = { navController.navigate("agregar_zapato") },
                            leadingIcon = { Icon(Icons.Default.Add, contentDescription = "Agregar") }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(zapatos) { zapato ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = zapato.imagen,
                            contentDescription = zapato.estilo,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = zapato.estilo, style = MaterialTheme.typography.titleLarge)
                            Text(text = "Precio: $${zapato.precio}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Cantidad: ${zapato.cantidad}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = zapato.descripcion, style = MaterialTheme.typography.bodySmall)
                        }
                        Column {
                            IconButton(onClick = { navController.navigate("editar_zapato/${zapato.id}") }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { viewModel.eliminarZapato(zapato) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                        Button(onClick = { viewModel.venderZapato(zapato) }) {
                            Text("-1")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventarioScreenPreview() {
    InventarioTheme {
        // No se puede previsualizar con navegación, así que esto es solo una vista previa de la UI.
    }
}
