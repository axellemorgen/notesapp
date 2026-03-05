package com.securenotes.app.ui.screens

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.securenotes.app.data.Note
import com.securenotes.app.security.BiometricSecurityManager
import com.securenotes.app.viewmodel.NoteViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Composable
fun SecureNotesScreen(
    securityManager: BiometricSecurityManager,
    viewModel: NoteViewModel,
    navController: NavController,
    onDismiss: () -> Unit
) {
    var authStep by remember { mutableStateOf(0) } // 0: choose method, 1: biometric, 2: PIN
    var secureNotes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isAuthenticated by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.secureNotes.collect { notes ->
            secureNotes = notes
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() }
    ) {
        if (!isAuthenticated) {
            AuthenticationDialog(
                securityManager = securityManager,
                onAuthenticated = { isAuthenticated = true },
                onDismiss = onDismiss
            )
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    TopAppBar(
                        title = { Text("Notes Secrètes") },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Fermer")
                            }
                        }
                    )

                    if (secureNotes.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Aucune note secrète",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(secureNotes) { note ->
                                NoteCard(
                                    note = note,
                                    onClick = { navController.navigate("note/${note.id}") },
                                    onDelete = { viewModel.deleteNote(note) },
                                    onToggleArchive = { viewModel.toggleArchive(note) },
                                    onTogglePin = { viewModel.togglePin(note.id, !note.isPinned) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuthenticationDialog(
    securityManager: BiometricSecurityManager,
    onAuthenticated: () -> Unit,
    onDismiss: () -> Unit
) {
    var authMethod by remember { mutableStateOf(0) } // 0: choose, 1: biometric, 2: PIN
    var pin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val canUseBiometric = securityManager.canUseBiometric()

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .align(Alignment.Center)
            .padding(24.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (authMethod == 0) {
                Text(
                    "Accès aux notes secrètes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                if (canUseBiometric) {
                    Button(
                        onClick = { authMethod = 1 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(bottom = 12.dp)
                    ) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Empreinte digitale")
                    }
                }

                Button(
                    onClick = { authMethod = 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Code PIN")
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text("Annuler")
                }
            } else if (authMethod == 1) {
                Text(
                    "Placez votre doigt sur le capteur",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Icon(
                    Icons.Default.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                TextButton(
                    onClick = { authMethod = 0 },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Utiliser le PIN")
                }

                LaunchedEffect(Unit) {
                    // Placeholder for biometric authentication
                    // In a real implementation, this would use BiometricPrompt
                }
            } else {
                Text(
                    "Entrez votre code PIN",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Code PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    isError = errorMessage.isNotEmpty()
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
                        scope.launch {
                            val storedPin = securityManager.getPIN().first()
                            if (pin == storedPin) {
                                onAuthenticated()
                            } else {
                                errorMessage = "Code PIN incorrect"
                                pin = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Text("Valider")
                }

                TextButton(
                    onClick = { authMethod = 0 },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retour")
                }
            }
        }
    }
}
