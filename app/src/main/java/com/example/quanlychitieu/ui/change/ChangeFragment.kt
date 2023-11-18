package com.example.quanlychitieu.ui.change

import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.base.BaseFragmentWithBinding
import com.example.quanlychitieu.model.Money
import com.example.quanlychitieu.model.Spending
import com.example.quanlychitieu.utils.Utils
import com.example.quanlychitieu.utils.click
import com.thn.quanlychitieu.databinding.FragmentChangeBinding

class ChangeFragment : BaseFragmentWithBinding<FragmentChangeBinding>() {

    companion object {
        fun newInstance() = ChangeFragment()
    }

    private lateinit var viewModel: ChangeViewModel
    private var money: Int = 0
    override fun getViewBinding(inflater: LayoutInflater): FragmentChangeBinding {
        return FragmentChangeBinding.inflate(layoutInflater).apply {
            viewModel = ViewModelProvider(
                this@ChangeFragment,
                ChangeViewModelFactory(requireContext())
            ).get(ChangeViewModel::class.java)

        }
    }

    override fun init() {
        setToolbar(binding.toolbar)
    }

    override fun initData() {
        viewModel.getMoney()
        viewModel.money.observe(viewLifecycleOwner) {
            this.money = it.money ?: 0
        }
    }

    override fun initAction() {
        binding.save.click {
            if (binding.content.text?.trim().isNullOrEmpty() || binding.category.text?.trim()
                    .isNullOrEmpty() || binding.money.text?.trim().isNullOrEmpty()
            ) {
                Toast.makeText(context, "Vui Lòng Nhập đầy đủ thông tin ", Toast.LENGTH_LONG).show()
            } else {
                if (!Utils.isValidDate(binding.date.text?.trim().toString())) {
                    Toast.makeText(
                        context,
                        "Vui lòng nhập ngày tháng năm theo định dạng ngày/tháng/năm",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    val spending = Spending(
                        money = binding.money.getMoney(),
                        title = binding.category.text.toString(),
                        content = binding.content.text.toString(),
                        date = binding.date.text.toString()
                    )
                    viewModel.setSpending(spending)
                    val money = Money(money = this.money.minus(binding.money.getMoney()))
                    viewModel.setMoney(money) {
                        mainActivity.onBackPressed()
                    }
                }
            }
        }
    }
}


