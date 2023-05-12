package com.example.quanlychitieu.ui

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.quanlychitieu.base.BaseActivity
import com.example.quanlychitieu.base.BaseFragment
import com.example.quanlychitieu.ui.add.AddFragment
import com.example.quanlychitieu.ui.home.HomeFragment
import com.example.quanlychitieu.ui.main.MainFragment
import com.example.quanlychitieu.ui.type_note.NoteTypeFragment
import com.thn.quanlychitieu.R
import com.thn.quanlychitieu.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainFragment = MainFragment.newInstance()
    val homeFragment = HomeFragment.newInstance()
    val addFragment = AddFragment.newInstance()
    val noteTypeFragment = NoteTypeFragment.newInstance()
    override fun getLayout(): Int = R.layout.activity_main


    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun init() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(MainFragment::class.java.name)
        if (fragment == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.fragment_container_view_tag,
                mainFragment,
                MainFragment::class.java.name
            ).commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.let {
                val fragment = it.fragments.get(it.fragments.size - 2) as BaseFragment
                fragment.onBackNavigation?.invoke()
            }
        }
        super.onBackPressed()
    }

    fun getListFragment(): ArrayList<Fragment> =
        arrayListOf(homeFragment, addFragment, noteTypeFragment)
}