package com.example.quanlychitieu.ui.type_note.edit_note

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
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class EditNoteViewModel(context: Context) : ViewModel() {
    private val repository: Repository = Repository(context)
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    private var _time: MutableLiveData<String> = MutableLiveData()
    val time: LiveData<String> get() = _time
    private var _titleLength: MutableLiveData<String> = MutableLiveData("  |  0 ký tự")
    val titleLength: LiveData<String> get() = _titleLength
    private var _listNote: MutableLiveData<List<Note>> = MutableLiveData()
    val listNote: LiveData<List<Note>> get() = _listNote
    init {
        getTime()
    }

    fun getTime() {
        scope.launch() {
            val calendar: Calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
            withContext(Dispatchers.Main) {
                _time.value = dateFormat.format(calendar.time)
            }
        }.start()
    }


    fun setTitleLength(length: Int) {
        _titleLength.value = "  |  $length ký tự"
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun update(note: Note) {
        scope.launch {
            repository.updateNote(note)
        }
    }
    fun add(note: Note){
        scope.launch {
            repository.addNote(note)
        }
    }
    fun getAllNote(){
        viewModelScope.launch {
            _listNote.postValue(repository.getAllNote() )
        }
    }

    class EditViewModelFactory(private val app:Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
                return EditNoteViewModel(app) as T
            }
            throw IllegalArgumentException("loiN")
        }


    }
}