package com.example.mynotes.repository

import androidx.lifecycle.LiveData
import com.example.mynotes.data.Note
import com.example.mynotes.data.NoteDao

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun update(note: Note){
        noteDao.update(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)

    }
//    fun searchNotes(searchQuery: String): LiveData<List<Note>> {
//        return noteDao.searchNotes(searchQuery)
//    }


}