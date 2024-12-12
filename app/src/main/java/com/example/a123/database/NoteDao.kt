package com.example.a123.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a123.model.DateUsage
import com.example.a123.model.Note
import com.example.a123.model.MoodImageUsage
import com.example.a123.model.MoodImageUsageByDate

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE :query OR noteDesc LIKE :query")
    fun searchNote(query: String?): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE moodImageId = :imageId")
    fun getNotesByMoodImage(imageId: Int): LiveData<List<Note>>

    @Query("SELECT moodImageId, COUNT(*) FROM NOTES GROUP BY moodImageId")
    fun getMoodImageUsageCounts(): LiveData<List<MoodImageUsage>>

    @Query("SELECT date, COUNT(*) as count FROM NOTES GROUP BY date")
    fun getNotesCountByDate(): LiveData<List<DateUsage>>

    @Query("SELECT date, moodImageId, COUNT(*) as count FROM NOTES GROUP BY date, moodImageId")
    fun getNotesCountByDateAndMood(): LiveData<List<MoodImageUsageByDate>>
}