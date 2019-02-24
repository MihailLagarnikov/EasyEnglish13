package ru.lagarnikov.easyenglish13.View

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentCreateCourseBinding


class FragmentCreateCourse:Fragment(),SeekBar.OnSeekBarChangeListener,View.OnClickListener {
    lateinit var binding: FragmentCreateCourseBinding
    lateinit var mModel: MyViewModel
    lateinit var mSeekBar:SeekBar
    private var mFlagClicButton=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_create_course,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(false)
        val myView=binding.root
        mSeekBar=binding.seekBar
        //Меняем цвет прогресса сикбара
        mSeekBar.getProgressDrawable().setColorFilter(resources.getColor(R.color.colorOrange), PorterDuff.Mode.MULTIPLY)
        //Меняем цвет значка сиикбара
        mSeekBar.getProgressDrawable()
            .setColorFilter(resources.getColor(R.color.colorOrange), PorterDuff.Mode.SRC_ATOP)
        mSeekBar.setOnSeekBarChangeListener(this)
        mSeekBar.progress= SEEKBAR_NORM
        binding.textViewNumberwords.setText(SEEKBAR_NORM.toString())
        binding.buttonChoseTheme1.setOnClickListener(this)
        binding.buttonChoseTheme2.setOnClickListener(this)

        return myView
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.d("asqs","1")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        Log.d("asqs","1")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        val s:String= mSeekBar.progress.toString()
        binding.textViewNumberwords.setText(s)
    }

    override fun onClick(v: View?) {
        if (!mFlagClicButton) {

            val numberWords:String= binding.textViewNumberwords.text as String

            InnerData.saveInt(CURENT_NUMBER_WARDS,numberWords.toInt())
            mModel.setNextFragmentName(FragmentChooseTheme())


            mFlagClicButton=true
        }
    }
}