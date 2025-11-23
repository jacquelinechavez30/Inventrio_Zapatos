package com.example.inventario.data

import com.example.inventario.model.Zapato
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import java.io.File

class ZapatosRepository(private val postgrest: Postgrest, private val storage: Storage) {

    suspend fun obtenerZapatos(): List<Zapato> {
        return postgrest.from("zapatos").select().decodeList<Zapato>()
    }

    suspend fun obtenerZapatoPorId(zapatoId: Int): Zapato? {
        return postgrest.from("zapatos").select {
            filter {
                eq("id", zapatoId)
            }
        }.decodeSingleOrNull<Zapato>()
    }

    suspend fun agregarZapato(zapato: Zapato) {
        postgrest.from("zapatos").insert(zapato)
    }

    suspend fun actualizarZapato(zapato: Zapato) {
        postgrest.from("zapatos").update(zapato) {
            filter {
                eq("id", zapato.id!!)
            }
        }
    }

    suspend fun eliminarZapato(zapatoId: Int) {
        postgrest.from("zapatos").delete {
            filter {
                eq("id", zapatoId)
            }
        }
    }

    suspend fun subirImagen(file: ByteArray, name: String): String {
        val path = "zapatos/$name"
        storage.from("zapatos").upload(path, file, upsert = true)
        return storage.from("zapatos").publicUrl(path)
    }
}