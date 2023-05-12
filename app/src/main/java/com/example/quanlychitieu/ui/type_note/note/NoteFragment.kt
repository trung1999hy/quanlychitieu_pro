package com.example.quanlychitieu.ui.type_note.note

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.ui.type_note.edit_note.EditNoteFragment
import com.example.quanlychitieu.utils.click
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thn.quanlychitieu.databinding.FragmentBottomSheetDialogBinding
import com.thn.quanlychitieu.databinding.FragmentNoteBinding
import java.util.Calendar

class NoteFragment : BaseFragmentWithBinding<FragmentNoteBinding>() {

    companion object {
        fun newInstance(noteType: String = "") = NoteFragment().apply {
            this.noteType = noteType
        }
    }

    private var noteAdapter = NoteAdapter({

        mainActivity.addFragment(
            this,
            EditNoteFragment.newInstance(note = it, it.type)
        )
    }, {
        showBottomSheetDialog(it)
    })

    private var noteType: String = ""
    private lateinit var viewModel: NoteViewModel
    private var calendar: Calendar = Calendar.getInstance()

    override fun getViewBinding(inflater: LayoutInflater): FragmentNoteBinding =
        FragmentNoteBinding.inflate(inflater).apply {
            viewModel = ViewModelProvider(
                this@NoteFragment,
                NoteViewModel.MainViewModelFactory(this@NoteFragment.requireContext())
            ).get(NoteViewModel::class.java)
        }

    override fun init() {
        setToolbar(binding.toolbarMain){
            mainActivity.noteTypeFragment.getCoin()
        }

        viewModel.getAllNote()
        binding?.recylerview?.adapter = noteAdapter
        binding?.recylerview?.setHasFixedSize(true)
    }





    override fun initAction() {
        binding?.fab?.setOnClickListener {
            mainActivity.addFragment(this, EditNoteFragment.newInstance(null, noteType))
        }
    }


    override fun initData() {
        viewModel.listNote.observe(this) {
            if (it.isNotEmpty()) {
                var listData: ArrayList<Note> = arrayListOf()
                it.reversed().forEach {
                    listData.add(it)
                }

                noteAdapter.setAdapter(listData.filter { it.type == noteType } as ArrayList<Note>)
            } else
                noteAdapter.setAdapter(arrayListOf())
        }
    }

    fun showBottomSheetDialog(note: Note) {
        val binding =
            FragmentBottomSheetDialogBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)
        binding.viewRemind.visibility = View.GONE
        binding.viewRemind.click {
            openDatePicker(note)
        }
        binding.viewRemove.click {
            removeNote(note)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun removeNote(note: Note) {
        val dialogRemove: AlertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Xóa ?")
            setMessage("Bạn có chắc muốn xóa ?")
            setPositiveButton("Đồng ý") { dialog, id ->
                viewModel.remove(note)
            }
            setNegativeButton("Đóng") { dialog, id ->
                dialog.dismiss()
            }
        }.create()
        dialogRemove.show()
    }

    private fun openDatePicker(note: Note) {
        DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                openTimePicker(note)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setTitle("")
            show()
        }
    }

    private fun openTimePicker(note: Note) {
        TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                note.timeSet = calendar.timeInMillis.toString()
                viewModel.update(note)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).apply {
            setTitle("")
            show()
        }

    }
}