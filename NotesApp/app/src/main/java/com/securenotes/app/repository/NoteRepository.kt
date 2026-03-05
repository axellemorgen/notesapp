package com.securenotes.app.repository

import com.securenotes.app.data.Note
import com.securenotes.app.data.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    
    fun getSecureNotes(): Flow<List<Note>> = noteDao.getSecureNotes()
    
    fun getArchivedNotes(): Flow<List<Note>> = noteDao.getArchivedNotes()
    
    suspend fun getNoteById(id: String): Note? = noteDao.getNoteById(id)
    
    suspend fun insertNote(note: Note) = noteDao.insert(note)
    
    suspend fun updateNote(note: Note) = noteDao.update(note)
    
    suspend fun deleteNote(note: Note) = noteDao.delete(note)
    
    fun getNotesByTag(tag: String): Flow<List<Note>> = noteDao.getNotesByTag("%$tag%")
    
    fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes("%$query%")
    
    fun getAllTags(): Flow<List<String>> = noteDao.getAllTags()
    
    suspend fun togglePin(noteId: String, isPinned: Boolean) = 
        noteDao.updatePinnedStatus(noteId, isPinned)
    
    suspend fun toggleArchive(note: Note) =
        noteDao.update(note.copy(isArchived = !note.isArchived))
    
    suspend fun toggleSecure(note: Note) =
        noteDao.update(note.copy(isSecure = !note.isSecure))
}
