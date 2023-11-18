package com.example.quanlychitieu.ui.type_note.note

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.ui.type_note.edit_note.EditNoteFragment
import com.example.quanlychitieu.utils.click
import com.example.quanlychitieu.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thn.quanlychitieu.databinding.DialogShetPasswordBinding
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

        if(it.password.isNullOrEmpty()) {
            mainActivity.addFragment(
                this,
                EditNoteFragment.newInstance(note = it, it.type)
            )
        }else{
            showInputPasswordDialog(it)
        }

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
        setToolbar(binding.toolbarMain) {
            mainActivity.noteTypeFragment.getData()
        }
        binding?.recylerview?.adapter = noteAdapter
        binding?.recylerview?.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding?.recylerview?.setHasFixedSize(true)
    }

    override fun initAction() {
        binding?.fab?.setOnClickListener {
            mainActivity.addFragment(this, EditNoteFragment.newInstance(null, noteType))
        }
    }


    override fun initData() {
        viewModel.getAllNote().observe(this) {
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
        val isSetPassword = note.password.isNullOrEmpty()
        val binding =
            FragmentBottomSheetDialogBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
        if (!isSetPassword) {
            binding.txtText.text = "Xóa mật khẩu"
        }
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)
        binding.viewRemind.visibility = View.GONE
        binding.viewRemind.click {
            openDatePicker(note)
        }
        binding.viewPassword.click {
            showSetPasswordDialog(note, isSetPassword)
            dialog.dismiss()
        }
        binding.viewRemove.click {
            removeNote(note)
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showInputPasswordDialog(note: Note){
        val binding = DialogShetPasswordBinding.inflate(
            LayoutInflater.from(requireContext()),
            null,
            false
        )
        binding.title.text = "Nhập mật khẩu"
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)
        binding.btnCancel.click {
            dialog.dismiss()
        }
        binding.btnOke.click {
            val password = binding.edPassword.text.toString()
            if (password.isNotEmpty() && password == note.password ){
                mainActivity.addFragment(
                    this,
                    EditNoteFragment.newInstance(note =note , note.type)
                )
                dialog.dismiss()
            } else {
               requireContext(). showToast("Mật khẩu không được để trống hoặc sai mật khẩu")
            }
        }
        dialog.show()
    }

    private fun showSetPasswordDialog(note: Note, isSetPassword: Boolean) {
        val dialogShetPasswordBinding =
            DialogShetPasswordBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setCancelable(true)

        if (!isSetPassword){
            dialogShetPasswordBinding.title.text ="Nhập mật khẩu để xóa"
        }
        dialog.setContentView(dialogShetPasswordBinding.root)

        dialogShetPasswordBinding.btnCancel.click {
            dialog.dismiss()
        }
        dialogShetPasswordBinding.btnOke.click {
            handleOkButtonClick(dialogShetPasswordBinding, note, isSetPassword)
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun handleOkButtonClick(binding: DialogShetPasswordBinding,note: Note, isSetPassword: Boolean) {
        if (!binding.edPassword.text.isNullOrEmpty()) {
            if (isSetPassword) {
                note.password = binding.edPassword.text.toString()
                updateNote(note)
                requireContext().showToast("Đặt mật khẩu thành công")
            } else {
                if(note.password != binding.edPassword.text.toString()){
                    requireContext().showToast("Mật khẩu nhập lại không đúng")
                }else{
                    note.password = ""
                    updateNote(note)
                    requireContext().showToast("Xóa mật khẩu thành công")
                }

            }
        }
    }

    private fun updateNote(note: Note){
        viewModel.update(note)
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