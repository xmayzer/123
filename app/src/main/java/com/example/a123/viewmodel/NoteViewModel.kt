package com.example.a123.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.a123.database.NoteDao
import com.example.a123.database.NoteDatabase
import com.example.a123.model.DateUsage
import com.example.a123.model.MoodImageUsage
import com.example.a123.model.MoodImageUsageByDate
import com.example.a123.model.Note
import com.example.a123.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(app:Application, private val noteRepository: NoteRepository): AndroidViewModel(app) {
    private val noteDao: NoteDao = NoteDatabase.createDatabase(app).getNoteDao()

    val moodImageUsageCounts: LiveData<List<MoodImageUsage>> = noteDao.getMoodImageUsageCounts()


    fun addNote(note: Note) =
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    fun deleteNote(note: Note) =
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    fun updateNote(note: Note) =
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    fun getAllNotes() = noteRepository.getAllNotes()


    fun getNotesCountByDateAndMood(): LiveData<List<MoodImageUsageByDate>> {
        return noteDao.getNotesCountByDateAndMood()
    }


}