package com.securenotes.app.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private val Context.dataStore by preferencesDataStore(name = "security")

class BiometricSecurityManager(private val context: Context) {
    private val PIN_KEY = stringPreferencesKey("user_pin")
    private val BIOMETRIC_ENABLED_KEY = stringPreferencesKey("biometric_enabled")
    private val KEYSTORE_ALIAS = "notes_key"
    private val KEY_SIZE = 256
    private val AUTHENTICATION_TAG_LENGTH_BIT = 128
    private val IV_LENGTH_BYTE = 12

    fun canUseBiometric(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    suspend fun setPIN(pin: String) {
        context.dataStore.edit { preferences ->
            preferences[PIN_KEY] = pin
        }
    }

    suspend fun verifyPIN(pin: String): Boolean {
        val storedPin = context.dataStore.data.map { preferences ->
            preferences[PIN_KEY] ?: ""
        }
        return storedPin.collect { stored ->
            pin == stored
        }
    }

    fun getPIN(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PIN_KEY] ?: ""
        }
    }

    suspend fun enableBiometric(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED_KEY] = if (enabled) "true" else "false"
        }
    }

    fun isBiometricEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[BIOMETRIC_ENABLED_KEY] == "true"
        }
    }

    fun isBiometricSetup(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PIN_KEY]?.isNotEmpty() == true
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return if (keyStore.containsAlias(KEYSTORE_ALIAS)) {
            keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            keyGenerator.init(KEY_SIZE)
            keyGenerator.generateKey()
        }
    }

    fun getCipher(): Cipher {
        return Cipher.getInstance("AES/GCM/NoPadding")
    }

    fun getCipherForDecryption(iv: ByteArray): Cipher {
        val cipher = getCipher()
        val spec = GCMParameterSpec(AUTHENTICATION_TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        return cipher
    }
}
