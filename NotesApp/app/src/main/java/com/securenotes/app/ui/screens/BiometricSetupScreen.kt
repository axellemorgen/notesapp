package com.securenotes.app.ui.screens

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.securenotes.app.security.BiometricSecurityManager
import kotlinx.coroutines.launch

@Composable
fun BiometricSetupScreen(
    securityManager: BiometricSecurityManager,
    canUseBiometric: Boolean,
    onSetupComplete: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: PIN setup, 2: Biometric setup
    var enableBiometric by remember { mutableStateOf(canUseBiometric) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuration Sécurité") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (step == 1) {
                // PIN Setup
                Text(
                    "Créer un code PIN de sécurité",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    "Ce code PIN vous permettra d'accéder à votre zone secrète si la biométrie n'est pas disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Code PIN (4-8 chiffres)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    isError = pin.isNotEmpty() && (pin.length < 4 || pin.any { !it.isDigit() })
                )

                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = { confirmPin = it },
                    label = { Text("Confirmer le PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    isError = confirmPin.isNotEmpty() && confirmPin != pin
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        when {
                            pin.isEmpty() || confirmPin.isEmpty() -> {
                                errorMessage = "Veuillez remplir les deux champs"
                            }
                            pin.length < 4 || pin.length > 8 -> {
                                errorMessage = "Le PIN doit contenir entre 4 et 8 chiffres"
                            }
                            pin.any { !it.isDigit() } -> {
                                errorMessage = "Le PIN ne doit contenir que des chiffres"
                            }
                            pin != confirmPin -> {
                                errorMessage = "Les PIN ne correspondent pas"
                            }
                            else -> {
                                scope.launch {
                                    securityManager.setPIN(pin)
                                    if (canUseBiometric) {
                                        step = 2
                                    } else {
                                        onSetupComplete()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(if (canUseBiometric) "Suivant" else "Terminer")
                }
            } else {
                // Biometric Setup
                Icon(
                    Icons.Default.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .padding(bottom = 24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Ajouter la reconnaissance d'empreinte",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    "Vous pouvez utiliser votre empreinte digitale pour un accès plus rapide à votre zone secrète.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                securityManager.enableBiometric(false)
                                onSetupComplete()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("Plus tard")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                securityManager.enableBiometric(true)
                                onSetupComplete()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Activer")
                    }
                }
            }
        }
    }
}
