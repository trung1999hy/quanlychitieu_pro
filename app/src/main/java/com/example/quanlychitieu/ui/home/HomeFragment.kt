package com.example.quanlychitieu.ui.home

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.ui.cash_wallet.CashWalletFragment
import com.example.quanlychitieu.ui.history_revenue_expenditure.HistoryRevenueExpenditureFragment
import com.example.quanlychitieu.ui.history_revenue_expenditure.collect_money.CollectMoneyAdapter
import com.example.quanlychitieu.ui.history_revenue_expenditure.spending_money.SpendingMoneyAdapter
import com.example.quanlychitieu.utils.click
import com.thn.quanlychitieu.databinding.FragmentHomeBinding


class HomeFragment : BaseFragmentWithBinding<FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private val collectMoneyAdapter: CollectMoneyAdapter = CollectMoneyAdapter()
    private val spendingAdapter = SpendingMoneyAdapter()


    override fun getViewBinding(inflater: LayoutInflater): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater).apply {
            viewModel = ViewModelProvider(
                this@HomeFragment,
                HomeViewModelFactory(requireContext())
            )[HomeViewModel::class.java]
        }


    override fun init() {
        viewModel.init()
        binding.rvList.adapter = spendingAdapter
        binding.rvListCollect.adapter = collectMoneyAdapter
        binding.rvList.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvListCollect.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
        viewModel.getTotalExpenditure()
        viewModel.getListSpending()
        viewModel.getLisCollect()

    }

    override fun initData() {

        viewModel.listColum.observe(viewLifecycleOwner) {
            binding.chart.setData(it.first, it.second)
        }
        viewModel.money.observe(viewLifecycleOwner) {
            binding.viewTotalMoney.money.text = (it.money ?: 0).toString().plus(" VND")
        }
        viewModel.listSpending.observe(viewLifecycleOwner) {
            spendingAdapter.setData(it.ifEmpty { listOf() })
            if (it.isNotEmpty()) {
                binding.rvList.visibility = View.VISIBLE
                binding.textErrSpneding.visibility = View.GONE
            } else {
                binding.rvList.visibility = View.GONE
                binding.textErrSpneding.visibility = View.VISIBLE
            }
        }

        viewModel.listCollect.observe(viewLifecycleOwner) {
            collectMoneyAdapter.setData(it.ifEmpty { listOf() })
            if (it.isNotEmpty()) {
                binding.rvListCollect.visibility = View.VISIBLE
                binding.textErrCollect.visibility = View.GONE
            } else {
                binding.rvListCollect.visibility = View.GONE
                binding.textErrCollect.visibility = View.VISIBLE
            }
        }

    }

    override fun initAction() {
        binding.viewTotalMoney.root.click {
            mainActivity.addFragment(this.requireParentFragment(), CashWalletFragment.newInstance())
        }
        binding.history.click {
            mainActivity.addFragment(
                this.requireParentFragment(),
                HistoryRevenueExpenditureFragment.newInstance()
            )
        }
        binding.rvList.setHasFixedSize(true)
    }

}