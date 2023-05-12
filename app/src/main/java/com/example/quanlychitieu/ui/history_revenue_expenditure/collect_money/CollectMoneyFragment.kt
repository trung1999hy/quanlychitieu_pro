package com.example.quanlychitieu.ui.history_revenue_expenditure.collect_money

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.thn.quanlychitieu.databinding.FragmentCollectMoneyBinding

class CollectMoneyFragment : BaseFragmentWithBinding<FragmentCollectMoneyBinding>() {

    companion object {
        fun newInstance() = CollectMoneyFragment()
    }

    private lateinit var adapter: CollectMoneyAdapter

    private lateinit var viewModel: CollectMoneyViewModel
    override fun getViewBinding(inflater: LayoutInflater): FragmentCollectMoneyBinding =
        FragmentCollectMoneyBinding.inflate(inflater).apply {
            viewModel =
                ViewModelProvider(
                    this@CollectMoneyFragment,
                    CollectMoneyViewModelFactory(requireContext())
                ).get(CollectMoneyViewModel::class.java)
        }


    override fun init() {
        adapter = CollectMoneyAdapter()
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun initData() {
        viewModel.getLisCollect()
        viewModel.listCollect.observe(viewLifecycleOwner) {
            adapter.setData(it.ifEmpty { listOf() })
            if (it.isNotEmpty()) {
                binding.rvList.visibility = View.VISIBLE
                binding.textErr.visibility = View.GONE
            } else {
                binding.rvList.visibility = View.GONE
                binding.textErr.visibility = View.VISIBLE
            }
        }
    }

    override fun initAction() {

    }

}