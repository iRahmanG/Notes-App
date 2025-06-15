package com.example.mynotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.mynotes.data.Note
import com.example.mynotes.data.NoteDatabase
import com.example.mynotes.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import androidx.lifecycle.Transformations

class NoteViewModel(application: Application) :AndroidViewModel(application) {

    private  val repository :NoteRepository
    val allNotes : LiveData<List<Note>>

    private val searchQuery = MutableLiveData<String>("")

//    val notesToDisplay :LiveData<List<Note>> = Transformations.switchMap(searchQuery) { query ->
//        if (query.isNullOrEmpty()) {
//            repository.allNotes
//        } else {
//            repository.searchNotes("%${query}%")
//        }
//    }


    init{
        val noteDao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun insert(note:Note) = viewModelScope.launch(Dispatchers.IO) { // insures DB calla are done on a background thread
        repository.insert(note)

    }
    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }
//    fun searchNotes(searchQuery: String): LiveData<List<Note>> {
//        return repository.searchNotes(searchQuery)
//    }


}