package com.example.quanlychitieu.ui.type_note.archive.edit_archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.model.Archive
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

class EditArchiveViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application)
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    private var _time: MutableLiveData<String> = MutableLiveData()
    val time: LiveData<String> get() = _time
    private var _titleLength: MutableLiveData<String> = MutableLiveData("  |  0 ký tự")
    val titleLength: LiveData<String> get() = _titleLength
    private var _listNote: MutableLiveData<List<Archive>> = MutableLiveData()
    val listNote: LiveData<List<Archive>> get() = _listNote
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

    fun update(item : Archive) {
        scope.launch {
            repository.updateArchive(item)
        }
    }
    fun add(item: Archive){
        scope.launch {
            repository.addArchive(item)
        }
    }

}