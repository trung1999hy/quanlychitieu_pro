package com.example.quanlychitieu.ui.type_note

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.model.NoteType
import com.example.quanlychitieu.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteTypeViewModel(context: Context) : ViewModel() {
    private val repository = Repository(context)
    private val _listNoteType: MutableLiveData<List<NoteType>> = MutableLiveData()
    val listNoteType: LiveData<List<NoteType>> = _listNoteType

    fun getListNoteType() {
        viewModelScope.launch {
            _listNoteType.postValue(repository.getListNoteType())
        }
    }
    fun setNoteType(noteType: NoteType){
        viewModelScope.launch {
            async {
                repository.addNoteType(noteType)
            }.await()
            getListNoteType()
        }
    }

    fun updateTitleNoteType(noteType: NoteType){
        viewModelScope.launch {
            async {
                repository.updateNoteType(noteType)
            }.await()
            getListNoteType()
        }
    }
    fun deleteNoteType(noteType: NoteType){
        viewModelScope.launch {
            async {
                repository.deleteNoteType(noteType)
            }.await()
            getListNoteType()
        }
    }

    class NoteTypeViewModelFactory(private val app: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteTypeViewModel::class.java)) {
                return NoteTypeViewModel(app) as T
            }
            throw IllegalArgumentException("loiN")
        }

    }
}