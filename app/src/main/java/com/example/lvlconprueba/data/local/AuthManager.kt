package com.example.lvlconprueba.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lvlconprueba.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val NOMBRE = stringPreferencesKey("nombre")
        private val USUARIO = stringPreferencesKey("usuario")
        private val ROL_CODIGO = stringPreferencesKey("rol_codigo")
        private val ROL = stringPreferencesKey("rol")
    }

    val userFlow: Flow<User?> = context.dataStore.data
        .map { preferences ->
            val token = preferences[TOKEN]
            if (token != null) {
                User(
                    token = token,
                    usuarioId = preferences[USER_ID] ?: "",
                    nombre = preferences[NOMBRE] ?: "",
                    usuario = preferences[USUARIO] ?: "",
                    rolCodigo = preferences[ROL_CODIGO],
                    rol = preferences[ROL]
                )
            } else {
                null
            }
        }.distinctUntilChanged()

    val isLoggedIn: Flow<Boolean> = userFlow
        .map { it != null }
        .distinctUntilChanged()

    suspend fun saveSession(user: User) {
        context.dataStore.edit { preferences ->
            user.token?.let { preferences[TOKEN] = it }
            user.usuarioId.let { preferences[USER_ID] = it }
            user.nombre.let { preferences[NOMBRE] = it }
            user.usuario.let { preferences[USUARIO] = it }
            user.rolCodigo?.let { preferences[ROL_CODIGO] = it }
            user.rol?.let { preferences[ROL] = it }
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getCurrentUser(): User? = userFlow.first()
}
