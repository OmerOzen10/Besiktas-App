package com.example.besiktasapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("favorites")

class FavoritesManager(private val context: Context) {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorites_key")

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun addFavorite(playerId: String){
        context.dataStore.edit { preferences->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = currentFavorites + playerId
        }
    }

    suspend fun removeFavorite(playerId: String){
        context.dataStore.edit { preference ->
            val currentFavorites = preference[FAVORITES_KEY] ?: emptySet()
            preference[FAVORITES_KEY] = currentFavorites - playerId
        }
    }
}