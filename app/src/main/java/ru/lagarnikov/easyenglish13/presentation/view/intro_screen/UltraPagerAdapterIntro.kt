package ru.lagarnikov.easyenglish13.presentation.view.intro_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.lagarnikov.easyenglish13.presentation.NUMBER_FRAGMENT_INTRO_CHILD

class UltraPagerAdapterIntro(fragmentManager: FragmentManager, val numberPage:Int):FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment=DialogIntroChildFRagment()
        val bundle=Bundle()
        bundle.putInt(NUMBER_FRAGMENT_INTRO_CHILD,position + 1)
        fragment.arguments=bundle
        return fragment
    }

    override fun getCount(): Int {
        return numberPage
    }
}