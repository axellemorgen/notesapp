package com.securenotes.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE isSecure = 0 AND isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isSecure = 1 AND isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getSecureNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes WHERE tags LIKE :tag AND isSecure = 0 AND isArchived = 0 ORDER BY updatedAt DESC")
    fun getNotesByTag(tag: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE (title LIKE :query OR content LIKE :query) AND isSecure = 0 AND isArchived = 0 ORDER BY updatedAt DESC")
    fun searchNotes(query: String): Flow<List<Note>>

    @Query("SELECT DISTINCT tags FROM notes WHERE tags != '' AND isSecure = 0")
    fun getAllTags(): Flow<List<String>>

    @Query("UPDATE notes SET isPinned = :pinned WHERE id = :id")
    suspend fun updatePinnedStatus(id: String, pinned: Boolean)
}
