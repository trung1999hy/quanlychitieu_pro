package com.example.quanlychitieu.ui.type_note

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.NoteType
import com.example.quanlychitieu.model.User
import com.example.quanlychitieu.ui.MainApp
import com.example.quanlychitieu.ui.inapp.PurchaseInAppActivity
import com.example.quanlychitieu.ui.type_note.archive.ArchiveFragment
import com.example.quanlychitieu.ui.type_note.note.NoteFragment
import com.example.quanlychitieu.utils.DataController
import com.example.quanlychitieu.utils.click
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thn.quanlychitieu.R
import com.thn.quanlychitieu.databinding.DialogMoreItemClickBinding
import com.thn.quanlychitieu.databinding.DialogNewFolderBinding
import com.thn.quanlychitieu.databinding.FragmentNoteTypeBinding

class NoteTypeFragment : BaseFragmentWithBinding<FragmentNoteTypeBinding>() {

    companion object {
        fun newInstance() = NoteTypeFragment().apply {
        }
    }

    private lateinit var viewModel: NoteTypeViewModel

    private lateinit var adapter: NoteTypeAdapter

    override fun getViewBinding(inflater: LayoutInflater): FragmentNoteTypeBinding {
        viewModel = ViewModelProvider(
            this,
            NoteTypeViewModel.NoteTypeViewModelFactory(this.requireContext())
        )[NoteTypeViewModel::class.java]
        return FragmentNoteTypeBinding.inflate(inflater)
    }


    override fun init() {
        getData()
        adapter = NoteTypeAdapter({
            showBottomSheetDialog(it)
        }, {
            mainActivity.addFragment(
                this.requireParentFragment(),
                NoteFragment.newInstance(it.title ?: "")
            )
        })
        binding.rvList.adapter = adapter

        binding.rvList.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun initData() {
        viewModel.getListNoteType()
        viewModel.listNoteType.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.texterr.visibility = View.GONE
                adapter.setData(it.reversed())
            } else {
                adapter.setData(listOf())
                binding.texterr.visibility = View.VISIBLE
            }
        }
    }

    override fun initAction() {
        binding.addFolder.click {
            showBottomSheetNewFolderDialog()
        }
        setMenuClick()
        openInApp()
    }

    private fun openInApp() {
        binding.coin.click {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
    }

    private fun showBottomSheetNewFolderDialog() {
        val binding =
            DialogNewFolderBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
        val dialog = BottomSheetDialog(requireContext())
        binding.cancel.click {
            dialog.dismiss()
        }
        binding.save.click {
            if (binding.title.text?.trim()?.isNotEmpty() == true) {
                val noteType = NoteType(title = binding.title.text?.trim().toString())
                viewModel.setNoteType(noteType)
                dialog.dismiss()
            }

        }
        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun showBottomSheetDialog(noteType: NoteType) {
        val binding = DialogMoreItemClickBinding.inflate(
            LayoutInflater.from(requireContext()),
            null,
            false
        )
        val dialog = BottomSheetDialog(requireContext())
        binding.viewEditButton.click {
            binding.viewButton.visibility = View.GONE
            binding.viewEdit.visibility = View.VISIBLE
            binding.title.setText(noteType.title)
        }
        binding.save.click {
            if (binding.title.text?.trim()?.isNotEmpty() == true) {
                noteType.title = binding.title.text?.trim().toString()
                viewModel.updateTitleNoteType(noteType)
                dialog.dismiss()
            }
        }
        binding.viewRemove.click {
            removeNoteType(noteType)
            dialog.dismiss()
        }

        binding.cancel.click {
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun removeNoteType(noteType: NoteType) {
        val dialogRemove: AlertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Xóa ?")
            setMessage("Bạn có chắc muốn xóa ?")
            setPositiveButton("Đồng ý") { dialog, id ->
                viewModel.deleteNoteType(noteType)
            }
            setNegativeButton("Đóng") { dialog, id ->
                dialog.dismiss()
            }
        }.create()
        dialogRemove.show()
    }

    private fun setMenuClick() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.archive -> {
                    mainActivity.addFragment(
                        this.requireParentFragment(),
                      ArchiveFragment.newInstance()
                    )
                    true
                }
                else -> false
            }
        }
    }


    private fun setDataBaseGold(){
        val dataController = DataController(MainApp.newInstance()?.deviceId?:"")
        dataController.writeNewUser(MainApp.newInstance()?.deviceId?:"",  100)
    }

     fun getData(){
        val dataController = DataController(MainApp.newInstance()?.deviceId?:"")
        dataController.setOnListenerFirebase(object : DataController.OnListenerFirebase {
            override fun onCompleteGetUser(user: User?) {
                user?.let {
                    MainApp.newInstance()?.preference?.setValueCoin(user.coin)
                    binding.coin.text = String.format(resources.getString(R.string.amount_gold), MainApp.newInstance()?.preference?.getValueCoin()  )
                } ?: kotlin.run {
                    setDataBaseGold()
                }
            }

            override fun onSuccess() {

            }

            override fun onFailure() {
                Toast.makeText(this@NoteTypeFragment.requireContext(), "Có lỗi kết nối đến server!", Toast.LENGTH_LONG).show()
            }
        })
        dataController.user
    }

}