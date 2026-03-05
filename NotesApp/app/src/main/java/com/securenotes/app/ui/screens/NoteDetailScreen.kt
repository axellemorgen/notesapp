package com.securenotes.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.securenotes.app.data.ChecklistItem
import com.securenotes.app.data.Note
import com.securenotes.app.viewmodel.NoteViewModel

@Composable
fun NoteDetailScreen(
    viewModel: NoteViewModel,
    noteId: String,
    navController: NavController,
    context: Context
) {
    var note by remember { mutableStateOf<Note?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(0xFFFFFFFF.toInt()) }
    var checklistItems by remember { mutableStateOf<List<ChecklistItem>>(emptyList()) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedAudios by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    var showColorPicker by remember { mutableStateOf(false) }
    var showFormatMenu by remember { mutableStateOf(false) }
    
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = selectedImages + uris
    }

    LaunchedEffect(noteId) {
        if (noteId.isNotEmpty()) {
            val fetchedNote = viewModel.allNotes.value.find { it.id == noteId } 
                ?: viewModel.secureNotes.value.find { it.id == noteId }
                ?: viewModel.archivedNotes.value.find { it.id == noteId }
            
            note = fetchedNote
            if (fetchedNote != null) {
                title = fetchedNote.title
                content = fetchedNote.content
                backgroundColor = fetchedNote.backgroundColor
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Éditer la note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (note != null) {
                                viewModel.updateNote(
                                    note!!.copy(
                                        title = title,
                                        content = content,
                                        backgroundColor = backgroundColor
                                    )
                                )
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Enregistrer")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Toolbar de formatage
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { showFormatMenu = !showFormatMenu },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Gras")
                }
                
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Italique")
                }
                
                IconButton(
                    onClick = { showColorPicker = !showColorPicker },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.FormatColorFill, contentDescription = "Couleur")
                }
                
                IconButton(
                    onClick = { imageLauncher.launch("image/*") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Ajouter image")
                }
                
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Audio")
                }
            }

            // Color picker
            if (showColorPicker) {
                ColorPicker(
                    currentColor = backgroundColor,
                    onColorSelected = { backgroundColor = it }
                )
            }

            // Title field
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )

            // Content field
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp),
                maxLines = 10
            )

            // Checklist section
            if (checklistItems.isNotEmpty()) {
                Text(
                    "Checklist",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                checklistItems.forEachIndexed { index, item ->
                    ChecklistItemRow(
                        item = item,
                        onCheckedChange = {
                            checklistItems = checklistItems.toMutableList().apply {
                                this[index] = item.copy(isChecked = it)
                            }
                        },
                        onDelete = {
                            checklistItems = checklistItems.filterIndexed { i, _ -> i != index }
                        }
                    )
                }
                
                Button(
                    onClick = { checklistItems = checklistItems + ChecklistItem() },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajouter une tâche")
                }
            }

            // Images display
            if (selectedImages.isNotEmpty()) {
                Text(
                    "Images",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                selectedImages.forEach { uri ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(uri.lastPathSegment ?: "Image", style = MaterialTheme.typography.bodySmall)
                        IconButton(onClick = { selectedImages = selectedImages - uri }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    currentColor: Int,
    onColorSelected: (Int) -> Unit
) {
    val colors = listOf(
        0xFFFFFFFF.toInt(),
        0xFFFFE5E5.toInt(),
        0xFFFFE5CC.toInt(),
        0xFFFFFFCC.toInt(),
        0xFFE5FFE5.toInt(),
        0xFFE5F2FF.toInt(),
        0xFFF0E5FF.toInt(),
        0xFFFFE5F0.toInt()
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(color))
                    .then(
                        if (color == currentColor) {
                            Modifier.border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                        } else {
                            Modifier
                        }
                    )
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun ChecklistItemRow(
    item: ChecklistItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
        Text(
            item.text,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isChecked) androidx.compose.ui.text.TextDecoration.LineThrough else androidx.compose.ui.text.TextDecoration.None
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}
