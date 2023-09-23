package com.example.quanlychitieu.ui.type_note.archive.edit_archive

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.Archive
import com.example.quanlychitieu.ui.type_note.edit_note.EditNoteFragment
import com.example.quanlychitieu.utils.click
import com.thn.quanlychitieu.R
import com.thn.quanlychitieu.databinding.FragmentEditArchiveragmentBinding

class EditArchiveFragment : BaseFragmentWithBinding<FragmentEditArchiveragmentBinding>() {

    companion object {
        fun newInstance() = EditArchiveFragment().apply {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var viewModel: EditArchiveViewModel

    private var archive: Archive? = null
    override fun getViewBinding(inflater: LayoutInflater): FragmentEditArchiveragmentBinding {
        return FragmentEditArchiveragmentBinding.inflate(inflater).apply {
            viewModel =
                ViewModelProvider(this@EditArchiveFragment).get(EditArchiveViewModel::class.java)
        }
    }

    override fun init() {
        setToolbar(binding.toolbar) {

        }
    }

    override fun initData() {
        viewModel.time.observe(this) {
            binding.time.text = it.toString()
        }
        binding?.title?.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.isNullOrEmpty() == false) {
                binding.check.visibility = View.VISIBLE
            } else {
                binding.check.visibility = View.GONE
            }
        }
        binding?.container?.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.isNullOrEmpty() == false) {
                binding.check.visibility = View.VISIBLE
            } else {
                binding.check.visibility = View.GONE
            }
        }

    }

    override fun initAction() {
        binding.check.click {
            if (archive != null) {
                archive?.title = (binding.title.text ?: "").toString()
                archive?.container = (binding.container.text ?: "").toString()
                viewModel.update(archive!!)
                binding.check.visibility = View.GONE
            } else {
                archive = Archive(
                    0,
                    (binding.title.text ?: "").toString(),
                    (binding.container.text ?: "").toString(),
                    time = (binding?.time?.text ?: "").toString()
                )
                viewModel.add(archive!!)
                binding.check.visibility = View.GONE
            }
        }
    }


}