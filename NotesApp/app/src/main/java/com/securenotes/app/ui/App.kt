package com.securenotes.app.ui

import android.app.Activity
import android.content.Context
import android.gesture.GestureOverlayView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.securenotes.app.security.BiometricSecurityManager
import com.securenotes.app.ui.screens.HomeScreen
import com.securenotes.app.ui.screens.NoteDetailScreen
import com.securenotes.app.ui.screens.BiometricSetupScreen
import com.securenotes.app.ui.screens.SecureNotesScreen
import com.securenotes.app.viewmodel.NoteViewModel
import com.securenotes.app.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun App(activity: Activity) {
    val context = activity.applicationContext
    val navController = rememberNavController()
    val viewModel: NoteViewModel = viewModel(factory = NoteViewModelFactory(context))
    val securityManager = remember { BiometricSecurityManager(context) }
    
    var showBiometricSetup by remember { mutableStateOf(false) }
    var dragFromTop by remember { mutableStateOf(false) }
    var showSecureNotes by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        securityManager.isBiometricSetup().collect { isSetup ->
            showBiometricSetup = !isSetup
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        // Detect swipe from top
                        if (change.position.y < 100 && dragAmount.y > 100) {
                            dragFromTop = true
                            showSecureNotes = true
                        }
                        change.consume()
                    }
                )
            }
    ) {
        if (showBiometricSetup) {
            BiometricSetupScreen(
                securityManager = securityManager,
                canUseBiometric = securityManager.canUseBiometric()
            ) {
                showBiometricSetup = false
            }
        } else if (showSecureNotes) {
            SecureNotesScreen(
                securityManager = securityManager,
                viewModel = viewModel,
                navController = navController,
                onDismiss = { showSecureNotes = false }
            )
        } else {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(viewModel = viewModel, navController = navController)
                }
                composable("note/{noteId}") { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId") ?: ""
                    NoteDetailScreen(
                        viewModel = viewModel,
                        noteId = noteId,
                        navController = navController,
                        context = context
                    )
                }
            }
        }
    }
}
