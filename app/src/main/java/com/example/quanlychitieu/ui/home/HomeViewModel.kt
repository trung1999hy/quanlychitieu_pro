package com.example.quanlychitieu.ui.home

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.model.Collect
import com.example.quanlychitieu.model.Money
import com.example.quanlychitieu.model.Spending
import com.example.quanlychitieu.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {
    private val repository: Repository = Repository(context)

    private val _money: MutableLiveData<Money> = MutableLiveData()
    val money: LiveData<Money> = _money

    private val _listColum: MutableLiveData<Pair<List<Triple<String, Float, Int>>, List<Double>>> =
        MutableLiveData()
    val listColum: LiveData<Pair<List<Triple<String, Float, Int>>, List<Double>>> = _listColum

    private val _listSpeding: MutableLiveData<List<Spending>> = MutableLiveData(arrayListOf())
    val listSpending: LiveData<List<Spending>> = _listSpeding

    private val _listCollect: MutableLiveData<List<Collect>> = MutableLiveData(arrayListOf())
    val listCollect: LiveData<List<Collect>> = _listCollect

    fun init() {
        viewModelScope.launch {
            var money: List<Money> = arrayListOf()
            val getDataMonet = async {
                money = repository.getMoney()
                if (money.isEmpty()) {
                    val addMoney = async {
                        repository.addMoney(Money(money = 0))
                        _money.postValue(Money(money = 0))

                    }
                    addMoney.await()
                } else _money.postValue(repository.getMoney().last())
            }
            getDataMonet.await()
        }
    }

    fun getListSpending() {
        viewModelScope.launch {
            val getData = async {
                repository.getListSpending().let {
                    if (it?.isNotEmpty() == true) {
                        _listSpeding.postValue(it)
                    } else _listSpeding.postValue(listOf())
                }
            }
            awaitAll(getData)

        }

    }


    fun getLisCollect() {
        viewModelScope.launch {
            val getData = async {
                repository.getListCollect().let {
                    if (it?.isNotEmpty() == true) {
                        _listCollect.postValue(it)
                    } else _listCollect.postValue(listOf())
                }
            }
            awaitAll(getData)
        }

    }

    fun getTotalExpenditure() {
        viewModelScope.launch {
            var list: ArrayList<Triple<String, Float, Int>> = arrayListOf()
            val listTotalMoney: ArrayList<Double> = arrayListOf()
            async {
                var total = 0
                repository.getListCollect().forEach {
                    if (it.money ?: 0 > 0) total += it.money!!
                }
                listTotalMoney.add(getMillionVND(total.toDouble()))
            }.await()
            async {
                var total = 0
                repository.getListSpending().forEach {
                    if (it.money ?: 0 > 0) total += it.money!!
                }
                listTotalMoney.add(getMillionVND(total.toDouble()))
            }.await()
            if (listTotalMoney[0] - listTotalMoney[1] > 0) {
                listTotalMoney.add(getNumber(listTotalMoney[0] - listTotalMoney[1]))
            } else listTotalMoney.add(getMillionVND(0.0))
            when (getComparisonMoneyCollectAndSpending(listTotalMoney)) {
                1 -> {
                    if (listTotalMoney.get(0) > 0)
                        list.add(Triple("Tổng Thu", 100f, Color.BLUE))
                    else
                        list.add(Triple("Tổng Thu", 1f, Color.BLUE))
                    if (listTotalMoney.get(1) > 0)
                        list.add(
                            Triple(
                                "Tổng Chi", getTotalRevenueVersusExpenditure(
                                    listTotalMoney.get(1),
                                    listTotalMoney.get(0)
                                ).toFloat(),
                                Color.GREEN
                            )
                        )
                    else list.add(
                        Triple(
                            "Tổng Chi",
                            1f,
                            Color.GREEN
                        )
                    )
                    list.add(
                        Triple(
                            "Tiết kiệm",
                            getTotalRevenueVersusExpenditure(
                                listTotalMoney.get(2),
                                listTotalMoney.get(0)
                            ).toFloat(),
                            Color.RED
                        )
                    )
                    _listColum.postValue(Pair(list, listTotalMoney))
                }

                0 -> {
                    if (listTotalMoney.get(0) > 0)
                        list.add(Triple("Tổng Thu", 100f, Color.BLUE))
                    else
                        list.add(Triple("Tổng Thu", 1f, Color.BLUE))
                    if (listTotalMoney.get(1) > 0)
                        list.add(
                            Triple(
                                "Tổng Chi",
                                100f,
                                Color.GREEN
                            )
                        )
                    else list.add(
                        Triple(
                            "Tổng Chi",
                            1f,
                            Color.GREEN
                        )
                    )
                    list.add(
                        Triple(
                            "Tiết kiệm",
                            getTotalRevenueVersusExpenditure(
                                listTotalMoney.get(0),
                                listTotalMoney.get(2)
                            ).toFloat(),
                            Color.RED
                        )
                    )
                    _listColum.postValue(Pair(list, listTotalMoney))
                }

                -1 -> {
                    if (listTotalMoney[0] > 0)
                        list.add(
                            Triple(
                                "Tổng Thu",
                                getTotalRevenueVersusExpenditure(
                                    listTotalMoney[0],
                                    listTotalMoney[1]
                                ).toFloat(), Color.BLUE
                            )
                        )
                    else
                        list.add(Triple("Tổng Thu", 1f, Color.BLUE))
                    if (listTotalMoney[1] > 0)
                        list.add(
                            Triple(
                                "Tổng Chi",
                                100f,
                                Color.GREEN
                            )
                        )
                    else list.add(
                        Triple(
                            "Tổng Chi",
                            1f,
                            Color.GREEN
                        )
                    )
                    if (listTotalMoney.get(2) > 0) {
                        list.add(
                            Triple(
                                "Tiết kiệm",
                                getTotalRevenueVersusExpenditure(
                                    listTotalMoney.get(2),
                                    listTotalMoney.get(1)
                                ).toFloat(),
                                Color.RED
                            )
                        )
                    } else {
                        list.add(
                            Triple(
                                "Tiết kiệm",
                                1f,
                                Color.RED
                            )
                        )
                    }
                    _listColum.postValue(Pair(list, listTotalMoney))
                }
            }
        }
    }

    fun getTotalRevenueVersusExpenditure(totalA: Double, totalB: Double): Double {
        return if (totalB > 0) {
            totalA / totalB * 100
        } else 1.0
    }


    fun getComparisonMoneyCollectAndSpending(listTotalMoney: ArrayList<Double>): Int {
        return (listTotalMoney.getOrNull(0) ?: 0.0).compareTo((listTotalMoney.getOrNull(1) ?: 0.0))
    }


    fun getMillionVND(money: Double): Double {

        val valueInMillions = if (money?.toDouble() == null) 0.0 else money.toDouble() / 1000000
        val formattedNumber = String.format("%.2f", valueInMillions)
        val integerPart = formattedNumber.substringBefore(",")
        val decimalPart = formattedNumber.substringAfter(",")
        val newNumber = (integerPart.toIntOrNull() ?: 0).toString() + "." + decimalPart
        return newNumber.toDoubleOrNull() ?: 0.0
    }

    fun getNumber(money: Double): Double {
        val formattedNumber = String.format("%.2f", money)
        val integerPart = formattedNumber.substringBefore(",")
        val decimalPart = formattedNumber.substringAfter(",")
        val newNumber = (integerPart.toIntOrNull() ?: 0).toString() + "." + decimalPart
        return newNumber.toDoubleOrNull() ?: 0.0
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}