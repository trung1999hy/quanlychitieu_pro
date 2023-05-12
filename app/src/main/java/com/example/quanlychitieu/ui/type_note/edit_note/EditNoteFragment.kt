package com.example.quanlychitieu.ui.type_note.edit_note

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider

import com.example.quanlychitieu.base.BaseFragmentWithBinding

import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.receiver.AlarmReceiver
import com.example.quanlychitieu.ui.MainApp
import com.example.quanlychitieu.utils.click
import com.example.quanlychitieu.utils.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.quanlychitieu.ui.inapp.PurchaseInAppActivity
import com.thn.quanlychitieu.R
import com.thn.quanlychitieu.databinding.FragmentBottomSheetDialogBinding
import com.thn.quanlychitieu.databinding.FragmentEditNoteBinding
import java.util.Calendar

class EditNoteFragment : BaseFragmentWithBinding<FragmentEditNoteBinding>() {

    companion object {
        fun newInstance(note: Note? = null, noteType: String = "") = EditNoteFragment().apply {
            this.note = note
            this.noteType = noteType
        }
    }

    private var menu: Menu? = null
    private var typeMenu: TypeMenu = TypeMenu.TYPE_DEFAULT
    private var showCheckTitle: Boolean = false
    private var showCheckContainer: Boolean = false
    private var datePickerDialog: DatePickerDialog? = null
    private var calendar: Calendar = Calendar.getInstance()
    private var note: Note? = null
    private var noteType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var viewModel: EditNoteViewModel
    override fun getViewBinding(inflater: LayoutInflater): FragmentEditNoteBinding =
        FragmentEditNoteBinding.inflate(inflater).apply {
            viewModel = ViewModelProvider(
                this@EditNoteFragment,
                EditNoteViewModel.EditViewModelFactory(requireContext())
            ).get(EditNoteViewModel::class.java)
        }


    override fun init() {
        setKeyboardVisibilityListener()
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = ""
    }


    override fun initData() {
        viewModel.time.observe(this) {
            binding?.time?.text = it
        }
        viewModel.titleLength.observe(this) {
            binding?.titleLength?.text = it
        }
        binding?.title?.setText(note?.title ?: "")
        binding?.container?.setText(note?.container ?: "")
    }


    private fun setTextChanged() {
        binding?.title?.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.isNullOrEmpty() == false) {
                if (typeMenu == TypeMenu.TYPE_TITLE) {
                    showCheckTitle = true
                    mainActivity.onPrepareOptionsMenu(menu).apply {
                        mainActivity.invalidateOptionsMenu()
                    }
                    viewModel.setTitleLength(text?.trim()?.length ?: 0)
                }
            } else {
                showCheckTitle = false
                mainActivity.onPrepareOptionsMenu(menu).apply {
                    mainActivity.invalidateOptionsMenu()
                }
            }
        }
        binding?.container?.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.isNullOrEmpty() == false) {
                if (typeMenu == TypeMenu.TYPE_CONTAINER) {
                    showCheckContainer = true
                    mainActivity.onPrepareOptionsMenu(menu).apply {
                        mainActivity.invalidateOptionsMenu()
                    }

                }
            } else {
                showCheckContainer = false
                mainActivity.onPrepareOptionsMenu(menu).apply {
                    mainActivity.invalidateOptionsMenu()
                }
            }
        }
    }

    private fun setFocusChangeListener() {
        binding?.title?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (binding?.title?.text?.trim()?.isNullOrEmpty() == false) {
                    showCheckTitle = true
                }
                setMenu(TypeMenu.TYPE_TITLE)
            } else {
                setMenu(TypeMenu.TYPE_DEFAULT)
                showCheckTitle = false
            }
            binding?.cardView2?.visibility = View.GONE
        }
        binding?.container?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (binding?.container?.text?.trim()?.isNullOrEmpty() == false) {
                    showCheckContainer = true
                }
                setMenu(TypeMenu.TYPE_CONTAINER)
                binding?.cardView2?.visibility = View.VISIBLE
            } else {
                showCheckContainer = false
                setMenu(TypeMenu.TYPE_CONTAINER)
                binding?.cardView2?.visibility = View.GONE
            }
        }
    }

    override fun initAction() {
        binding?.view?.setOnClickListener {
            binding?.container?.requestFocus()
            binding?.container?.showKeyboard(requireContext())
            if (binding?.title?.text?.trim()?.isNullOrEmpty() == false) {
                showCheckTitle = true
            }
            setMenu(TypeMenu.TYPE_CONTAINER)
            binding?.cardView2?.visibility = View.VISIBLE
        }
        binding?.clear?.setOnClickListener {

        }
        setFocusChangeListener()
        setTextChanged()
    }

    private fun setKeyboardVisibilityListener() {
        val parentView = (binding.root.findViewById<View>(android.R.id.content) as? ViewGroup)
        parentView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            private val rect: Rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView?.resources?.displayMetrics
                ).toInt()
                parentView?.getWindowVisibleDisplayFrame(rect)
                val heightDiff: Int = parentView?.rootView?.height ?: 0 - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {
                    return
                }
                alreadyOpen = isShown
                if (!isShown) {
                    showCheckTitle = false
                    showCheckContainer = false
                    setMenu(TypeMenu.TYPE_DEFAULT)
                    binding?.cardView2?.visibility = View.GONE
                }
            }
        })
    }

    private fun setMenu(typeMenu: TypeMenu) {
        this.typeMenu = typeMenu
        mainActivity.onPrepareOptionsMenu(menu).apply {
            mainActivity.invalidateOptionsMenu()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mainActivity.onBackPressed()
                false
            }

            R.id.checked -> {
                if (typeMenu == TypeMenu.TYPE_TITLE || typeMenu == TypeMenu.TYPE_CONTAINER) {
                    if (note != null) {
                        val noteData: List<Note> = viewModel.listNote.value?.filter {
                            it.title == note?.title && it.container == note?.container && it.time == note?.time
                        } ?: listOf()
                        val note = noteData?.getOrNull(0)?.id?.let {
                            Note(
                                id = it,
                                title = ((binding?.title?.text ?: "").toString()),
                                container = (binding?.container?.text ?: "").toString(),
                                time = (binding?.time?.text ?: "").toString(),
                                type = noteType,
                                timeSet = if (binding?.viewRemind?.isVisible == true) calendar.time.time.toString() else ""
                            )
                        }
                        note?.let {
                            this.note = note
                            viewModel.update(it)
                        }
                    } else {
                        val note = Note(
                            title = ((binding?.title?.text ?: "").toString()),
                            container = (binding?.container?.text ?: "").toString(),
                            time = (binding?.time?.text ?: "").toString(),
                            type = noteType,
                            timeSet = if (binding?.viewRemind?.isVisible == true) calendar.time.time.toString() else ""
                        )
                        this.note = note
                        viewModel.add(note!!)
                    }
                }
                true
            }

            R.id.more -> {
                showBottomSheetDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showBottomSheetDialog() {
        val binding =
            FragmentBottomSheetDialogBinding.inflate(
                LayoutInflater.from(requireContext()),
                null,
                false
            )
        val dialogbottomshet = BottomSheetDialog(requireContext())
        dialogbottomshet.setContentView(binding.root)
        binding.viewRemind.click {
            val dialog = AlertDialog.Builder(requireContext())

            dialog.setMessage("Bạn có muốn đặt nhắc nhở không ?")
                .setTitle("Đặt nhắc nhở ?")
            dialog.setPositiveButton("Oke") { dialog, which ->
                MainApp.newInstance()?.preference?.apply {
                    if (getValueCoin() > 1) {
                        setValueCoin(getValueCoin() - 1)
                        openDatePicker()
                        Toast.makeText(
                            requireContext(),
                            "Đã thêm  thành công và trù 1 vàng",
                            Toast.LENGTH_SHORT

                        ).show()
                    } else startActivity(
                        Intent(
                            requireContext(),
                            PurchaseInAppActivity::class.java
                        )
                    )
                }

            }
            dialog.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }.create()
            dialog.show()
        }
        dialogbottomshet.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        this.menu = menu
        when (typeMenu) {
            TypeMenu.TYPE_DEFAULT -> {
                setHideMenu(0, 1, 2, menu = menu)
                setShowMenu(3, menu = menu)
            }

            TypeMenu.TYPE_TITLE -> {
                setHideMenu(0, 1, 2, menu = menu)
                setShowMenu(3, menu = menu)
            }

            TypeMenu.TYPE_CONTAINER -> {
                setHideMenu(2, menu = menu)
                setShowMenu(0, 1, 3, menu = menu)
            }

            else -> {
                setHideMenu(0, 1, 2, menu = menu)
                setShowMenu(3, menu = menu)
            }
        }
        if (showCheckTitle) setShowMenu(2, menu = menu)
        if (showCheckContainer) setShowMenu(2, menu = menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setHideMenu(vararg id: Int, menu: Menu?) {
        id.forEach {
            menu?.get(it)?.isVisible = false
        }
    }

    private fun setShowMenu(vararg id: Int, menu: Menu?) {
        id.forEach {
            menu?.get(it)?.isVisible = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openDatePicker() {
        datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                openTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog?.setTitle("")
        datePickerDialog?.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openTimePicker() {
        TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val alarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireActivity(), AlarmReceiver::class.java)
                intent.action = "FOO_ACTION"
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                val alarmTimeAtUTC = calendar.timeInMillis
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)
                binding?.viewRemind?.visibility = View.VISIBLE
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).apply {
            setTitle("")
            show()
        }
    }


    enum class TypeMenu {
        TYPE_DEFAULT, TYPE_TITLE, TYPE_CONTAINER
    }
}