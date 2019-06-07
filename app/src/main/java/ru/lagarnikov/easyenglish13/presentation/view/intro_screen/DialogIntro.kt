package ru.lagarnikov.easyenglish13.presentation.view.intro_screen

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.UltraViewPagerAdapter
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.IntroParentDialogBinding
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import ru.lagarnikov.easyenglish13.presentation.NUMBER_CHILD_IN_INTRO
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen

class DialogIntro: Fragment() {
    private lateinit var ultraViewPager: UltraViewPager
    private lateinit var mViewModelStartScreen: ViewModelStartScreen


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:IntroParentDialogBinding=DataBindingUtil
            .inflate(inflater,R.layout.intro_parent_dialog,container,false)
        ultraViewPager=binding.ultraViewpagerIntro
        createUltraViewPager()
        mViewModelStartScreen =  ViewModelProviders.of(this.activity!!).get(ViewModelStartScreen::class.java)
        return binding.root
    }

    private fun createUltraViewPager(){

        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
//initialize UltraPagerAdapter，and add child view to UltraViewPager
        val adapter = UltraViewPagerAdapter(
            UltraPagerAdapterIntro(
                childFragmentManager, NUMBER_CHILD_IN_INTRO
            )
        )
        ultraViewPager.adapter = adapter

//initialize built-in indicator
        ultraViewPager.initIndicator()
//set style of indicators
        ultraViewPager.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(resources.getColor(R.color.newMainColor))
            .setNormalColor(Color.WHITE)
            .setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt())
//set the alignment
        ultraViewPager.indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
//construct built-in indicator, and add it to  UltraViewPager
        ultraViewPager.indicator.build()

//set an infinite loop
        ultraViewPager.setInfiniteLoop(false)


    }

    internal fun finishDialog(){
        mViewModelStartScreen.mNavigationPresentor.setNextFragmentName(FragmentName.StartFragment,true)

    }
}