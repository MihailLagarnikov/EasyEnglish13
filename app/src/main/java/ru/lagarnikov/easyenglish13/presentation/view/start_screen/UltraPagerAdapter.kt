package ru.lagarnikov.easyenglish13.presentation.view.start_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.lagarnikov.easyenglish13.data.model.getAllThemeData
import ru.lagarnikov.easyenglish13.presentation.MAX_WORDS_THEME_CHILD
import ru.lagarnikov.easyenglish13.presentation.POSITION_THEME_CHILD
import ru.lagarnikov.easyenglish13.presentation.THEME_IMAGE_RES_CHILD
import ru.lagarnikov.easyenglish13.presentation.THEME_NAME_CHILD

class UltraPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    private val mListData= getAllThemeData()
    override fun getItem(position: Int): Fragment {
        val fragment=StartScreenFragmentChild()
        fragment.arguments=getBundl(position)
        Log.d("asqs",position.toString())
        return fragment
    }

    override fun getCount(): Int {
        return mListData.size
    }

    private fun getBundl(position: Int):Bundle{
        val data=mListData.get(position)
        val  bundle=Bundle()
        bundle.putInt(THEME_NAME_CHILD,data.themeName)
        bundle.putInt(THEME_IMAGE_RES_CHILD,data.imag)
        bundle.putInt(MAX_WORDS_THEME_CHILD,data.numberWords)
        bundle.putInt(POSITION_THEME_CHILD,position)

        return bundle
    }
}