package com.securenotes.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.securenotes.app.data.Note
import com.securenotes.app.data.NotesDatabase
import com.securenotes.app.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    
    private val _allNotes: StateFlow<List<Note>> = MutableStateFlow(emptyList())
    val allNotes: StateFlow<List<Note>> = _allNotes
    
    private val _secureNotes: StateFlow<List<Note>> = MutableStateFlow(emptyList())
    val secureNotes: StateFlow<List<Note>> = _secureNotes
    
    private val _archivedNotes: StateFlow<List<Note>> = MutableStateFlow(emptyList())
    val archivedNotes: StateFlow<List<Note>> = _archivedNotes
    
    private val _searchResults: StateFlow<List<Note>> = MutableStateFlow(emptyList())
    val searchResults: StateFlow<List<Note>> = _searchResults
    
    private val _allTags: StateFlow<List<String>> = MutableStateFlow(emptyList())
    val allTags: StateFlow<List<String>> = _allTags

    init {
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                (_allNotes as MutableStateFlow).value = notes
            }
        }
        viewModelScope.launch {
            repository.getSecureNotes().collect { notes ->
                (_secureNotes as MutableStateFlow).value = notes
            }
        }
        viewModelScope.launch {
            repository.getArchivedNotes().collect { notes ->
                (_archivedNotes as MutableStateFlow).value = notes
            }
        }
        viewModelScope.launch {
            repository.getAllTags().collect { tags ->
                val parsedTags = tags.flatMap { it.split(",") }.map { it.trim() }.filter { it.isNotEmpty() }.distinct()
                (_allTags as MutableStateFlow).value = parsedTags
            }
        }
    }

    fun createNote(title: String, content: String, tags: String = "", backgroundColor: Int = 0xFFFFFFFF.toInt()) {
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                tags = tags,
                backgroundColor = backgroundColor
            )
            repository.insertNote(newNote)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun togglePin(noteId: String, isPinned: Boolean) {
        viewModelScope.launch {
            repository.togglePin(noteId, isPinned)
        }
    }

    fun toggleArchive(note: Note) {
        viewModelScope.launch {
            repository.toggleArchive(note)
        }
    }

    fun toggleSecure(note: Note) {
        viewModelScope.launch {
            repository.toggleSecure(note)
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                (_searchResults as MutableStateFlow).value = emptyList()
            } else {
                repository.searchNotes(query).collect { results ->
                    (_searchResults as MutableStateFlow).value = results
                }
            }
        }
    }

    fun filterByTag(tag: String) {
        viewModelScope.launch {
            repository.getNotesByTag(tag).collect { notes ->
                (_searchResults as MutableStateFlow).value = notes
            }
        }
    }
}

class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            val database = NotesDatabase.getDatabase(context)
            val dao = database.noteDao()
            val repository = NoteRepository(dao)
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
