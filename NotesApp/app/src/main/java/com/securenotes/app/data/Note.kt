package com.securenotes.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false,
    val isSecure: Boolean = false,
    val tags: String = "", // Comma-separated tags
    val backgroundColor: Int = 0xFFFFFFFF.toInt(), // Color
    val isPinned: Boolean = false
)

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String = "",
    val isChecked: Boolean = false,
    val parentId: String = "", // Pour les sous-tâches
    val indentLevel: Int = 0
)

data class NoteContent(
    val text: String = "",
    val checklists: List<ChecklistItem> = emptyList(),
    val imageUris: List<String> = emptyList(),
    val audioUris: List<String> = emptyList()
)
