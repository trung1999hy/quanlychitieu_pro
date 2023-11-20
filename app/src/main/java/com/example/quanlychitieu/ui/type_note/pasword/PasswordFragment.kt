package com.example.quanlychitieu.ui.type_note.pasword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.thn.quanlychitieu.R
import com.thn.quanlychitieu.databinding.FragmentPasswordBinding

class PasswordFragment : BaseFragmentWithBinding<FragmentPasswordBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentPasswordBinding {
        return FragmentPasswordBinding.inflate(inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun init() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {

            }
    }
}