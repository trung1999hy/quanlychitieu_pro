package com.example.quanlychitieu.ui.type_note.archive

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.Archive
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.ui.type_note.archive.edit_archive.EditArchiveFragment
import com.example.quanlychitieu.ui.type_note.edit_note.EditNoteFragment
import com.example.quanlychitieu.ui.type_note.note.NoteAdapter
import com.example.quanlychitieu.utils.click
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thn.quanlychitieu.databinding.FragmentArchiveBinding
import com.thn.quanlychitieu.databinding.FragmentBottomSheetDialogBinding

class ArchiveFragment : BaseFragmentWithBinding<FragmentArchiveBinding>() {

    companion object {
        fun newInstance() = ArchiveFragment()
    }

    private lateinit var viewModel: ArchiveViewModel
    override fun getViewBinding(inflater: LayoutInflater): FragmentArchiveBinding {
        return FragmentArchiveBinding.inflate(inflater, null, false).apply {
            viewModel = ViewModelProvider(this@ArchiveFragment).get(ArchiveViewModel::class.java)
        }
    }

    private var noteAdapter = ArchiveAdapter({

        mainActivity.addFragment(
            this,
            EditArchiveFragment.newInstance()
        )
    }, {
        showBottomSheetDialog(it)
    })


    private fun showBottomSheetDialog(note: Archive) {
        val binding =
            FragmentBottomSheetDialogBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)
        binding.viewRemind.visibility = View.GONE
        binding.viewRemove.click {
            removeNote(note)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun removeNote(note: Archive) {
        val dialogRemove: AlertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Xóa ?")
            setMessage("Bạn có chắc muốn xóa ?")
            setPositiveButton("Đồng ý") { dialog, id ->
//                viewModel.remove(note)
            }
            setNegativeButton("Đóng") { dialog, id ->
                dialog.dismiss()
            }
        }.create()
        dialogRemove.show()
    }

    override fun init() {
        setToolbar(binding.toolbarMain) {
            mainActivity.noteTypeFragment.getData()
        }
        binding?.recylerview?.adapter = noteAdapter
        binding?.recylerview?.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding?.recylerview?.setHasFixedSize(true)
    }

    override fun initData() {
        viewModel.getAllLiveData().observe(this) {
            if (it.isNotEmpty()) {
                val listData: ArrayList<Archive> = arrayListOf()
                it.reversed().forEach {
                    listData.add(it)
                }

                noteAdapter.setAdapter(listData )
            } else
                noteAdapter.setAdapter(arrayListOf())
        }
    }

    override fun initAction() {
        binding?.fab?.setOnClickListener {
            mainActivity.addFragment(this, EditArchiveFragment.newInstance())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}