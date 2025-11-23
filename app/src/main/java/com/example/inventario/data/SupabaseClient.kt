package com.example.inventario.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://rftocivukbkdktmsvuzy.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJmdG9jaXZ1a2JrZGt0bXN2dXp5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM4NDcxMzUsImV4cCI6MjA3OTQyMzEzNX0.FIEOumc3oAKpDkkEwyzgNua39IslP10uBbR716X2vcY"
    ) {
        install(Postgrest)
        install(Storage)
    }
}