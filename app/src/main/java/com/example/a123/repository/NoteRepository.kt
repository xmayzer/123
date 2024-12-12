package com.example.a123.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.a123.database.NoteDatabase
import com.example.a123.model.MoodImageUsageByDate
import com.example.a123.model.Note

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) {
        val noteWithClearedTime = note.copy(date = note.getDateWithoutTime())
        db.getNoteDao().insertNote(noteWithClearedTime)
    }

    suspend fun deleteNote(note: Note) =db.getNoteDao().deleteNote(note)

    suspend fun updateNote(note: Note) {
        val noteWithClearedTime = note.copy(date = note.getDateWithoutTime())
        db.getNoteDao().updateNote(noteWithClearedTime)
    }

    fun getAllNotes() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)


}