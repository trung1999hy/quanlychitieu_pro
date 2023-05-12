package com.example.quanlychitieu.ui.type_note.note

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoteViewModel(context: Context) : ViewModel() {
    private val repository: Repository = Repository(context)
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    private var _listNote: MutableLiveData<List<Note>> = MutableLiveData()
    val listNote: LiveData<List<Note>> get() = _listNote


    fun update(note: Note) {
        scope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun remove(note: Note) {
        scope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    fun getAllNote(){
        viewModelScope.launch {
            _listNote.postValue(repository.getAllNote() )
        }
    }

    class MainViewModelFactory(private val app: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(app) as T
            }
            throw IllegalArgumentException("loiN")
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}