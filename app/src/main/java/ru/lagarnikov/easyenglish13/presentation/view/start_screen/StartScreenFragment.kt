package ru.lagarnikov.easyenglish13.presentation.view.start_screen

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tmall.ultraviewpager.UltraViewPager
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.FragmentStartBinding
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen
import android.view.Gravity
import android.util.TypedValue
import androidx.lifecycle.Observer
import com.tmall.ultraviewpager.UltraViewPagerAdapter
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.presentation.SAVE_POSITION_THEME
import ru.lagarnikov.easyenglish13.presentation.view.finish_screen.DialogFinish


class StartScreenFragment:Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var mViewModelStartScreen: ViewModelStartScreen
    private lateinit var ultraViewPager:UltraViewPager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_start,
            container, false)
        mViewModelStartScreen =  ViewModelProviders.of(this!!.activity!!).get(ViewModelStartScreen::class.java)
        val myView=binding.root
        ultraViewPager=binding.ultraViewpager
        createUltraViewPager()
        createObservIsBackgroundWork()
        binding.progressBarStartFragment.visibility=View.INVISIBLE
        isSizeLessThen4_6()


        return myView
    }

    private fun isSizeLessThen4_6(){

        if(smartfonSize() <= 4.6){
            binding.imageView.visibility=View.GONE
        }

    }

    private fun smartfonSize():Double{
        val dm = DisplayMetrics()
        activity!!.getWindowManager().getDefaultDisplay().getMetrics(dm)

        val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
        val screenInches = Math.sqrt(x + y)
        return screenInches
    }

    private fun isLessonFinish(){
        if (mViewModelStartScreen.isLessonDone){
            mViewModelStartScreen.isLessonDone=false
            val dialogFinish=DialogFinish()
            dialogFinish.show(activity!!.supportFragmentManager,"0")
        }
    }

    fun startLesson(themeName:String){
        mViewModelStartScreen.startLesson(themeName)


    }

    fun repitLesson(themeName:String){
        mViewModelStartScreen.repitLesson(themeName)
    }



    private fun createUltraViewPager(){

        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
//initialize UltraPagerAdapter，and add child view to UltraViewPager
        val adapter = UltraViewPagerAdapter(UltraPagerAdapter(childFragmentManager))
        ultraViewPager.adapter = adapter

//initialize built-in indicator
        ultraViewPager.initIndicator()
//set style of indicators
        ultraViewPager.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(resources.getColor(R.color.newMainColor))
            .setNormalColor(Color.WHITE)
            .setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt())
//set the alignment
        ultraViewPager.indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
//construct built-in indicator, and add it to  UltraViewPager
        ultraViewPager.indicator.build()

//set an infinite loop
        ultraViewPager.setInfiniteLoop(false)
        ultraViewPager.setCurrentItem(InnerData.loadInt(SAVE_POSITION_THEME))



    }





    private fun createObservIsBackgroundWork(){
        val backgraundObser= Observer<Boolean> { newBoolean ->
            ifStartBackgraundWork(newBoolean) }
        mViewModelStartScreen.getIsBackgroundWork().observe(this,backgraundObser)
    }

    private fun ifStartBackgraundWork(isStart:Boolean){

        binding.progressBarStartFragment.visibility=getVisiblFromBoolean(isStart)
        binding.ultraViewpager.visibility=getVisiblFromBoolean(!isStart)

    }

    private fun getVisiblFromBoolean(visibl:Boolean):Int{
        return if (visibl){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        isLessonFinish()

    }


}