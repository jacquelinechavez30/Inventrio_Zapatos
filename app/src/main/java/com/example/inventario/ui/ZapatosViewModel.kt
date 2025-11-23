package com.example.inventario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.SupabaseClient
import com.example.inventario.data.ZapatosRepository
import com.example.inventario.model.Zapato
import io.github.jan.supabase.postgrest.postgrest
//import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ZapatosViewModel : ViewModel() {

    private val zapatosRepository = ZapatosRepository(SupabaseClient.client.postgrest, SupabaseClient.client.storage)

    private val _zapatos = MutableStateFlow<List<Zapato>>(emptyList())
    val zapatos: StateFlow<List<Zapato>> = _zapatos

    private val _selectedZapato = MutableStateFlow<Zapato?>(null)
    val selectedZapato: StateFlow<Zapato?> = _selectedZapato

    init {
        obtenerZapatos()
    }

    fun obtenerZapatos() {
        viewModelScope.launch {
            val zapatos = zapatosRepository.obtenerZapatos()
            _zapatos.value = zapatos
        }
    }

    fun getZapatoById(zapatoId: Int) {
        viewModelScope.launch {
            _selectedZapato.value = zapatosRepository.obtenerZapatoPorId(zapatoId)
        }
    }

    fun agregarZapato(zapato: Zapato) {
        viewModelScope.launch {
            zapatosRepository.agregarZapato(zapato)
            obtenerZapatos() // Refresh the list
        }
    }

    fun actualizarZapato(zapato: Zapato) {
        viewModelScope.launch {
            zapatosRepository.actualizarZapato(zapato)
            obtenerZapatos() // Refresh the list
        }
    }

    fun venderZapato(zapato: Zapato) {
        viewModelScope.launch {
            val nuevaCantidad = zapato.cantidad - 1
            if (nuevaCantidad >= 0) {
                zapatosRepository.actualizarCantidad(zapato.id!!, nuevaCantidad)
                obtenerZapatos() // Refresh the list
            }
        }
    }

    fun eliminarZapato(zapato: Zapato) {
        viewModelScope.launch {
            if (zapato.id != null) {
                zapatosRepository.eliminarZapato(zapato.id)
                obtenerZapatos() // Refresh the list
            }
        }
    }

    suspend fun subirImagen(file: ByteArray, name: String): String {
        return zapatosRepository.subirImagen(file, name)
    }
}