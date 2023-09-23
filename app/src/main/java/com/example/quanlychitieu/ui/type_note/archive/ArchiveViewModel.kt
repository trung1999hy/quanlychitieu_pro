package com.example.quanlychitieu.ui.type_note.archive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.repository.Repository

class ArchiveViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    fun getAllLiveData() = repository.getArchiveLiveData()



}