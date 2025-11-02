package com.example.tsoy_konstantin_app

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FavoriteCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val dataStore = context.dataStore
    private val FAVORITES_KEY = androidx.datastore.preferences.core.stringPreferencesKey("favorites")

    override suspend fun doWork(): Result {
        try {
            val favorites = dataStore.data
                .map { prefs ->
                    prefs[FAVORITES_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
                }.first()

            // Для примера — просто выводим в лог
            Log.d("FavoriteCheckWorker", "Current favorites: $favorites")

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
