package ru.lagarnikov.easyenglish13.view

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentCreateCourseBinding


class FragmentCreateCourseB:Fragment(),SeekBar.OnSeekBarChangeListener,View.OnClickListener {
    lateinit var binding: FragmentCreateCourseBinding
    lateinit var mModel: MyViewModel
    lateinit var mSeekBar:SeekBar
    private var mFlagClicButton=false
    private val SAD_GIRL_NUMBER=15 //после этого значения девушка становится грустной, меняется фото


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
        if(InnerData.loadInt(CURENT_NUMBER_WARDS)==0) {
            mSeekBar.progress = SEEKBAR_NORM
        }else{
            mSeekBar.progress=InnerData.loadInt(CURENT_NUMBER_WARDS)
        }
        val s:String= mSeekBar.progress.toString()

        binding.textViewNumberwords.setText(s)
        binding.nextGoGoGo.setOnClickListener(this)
        binding.textView18NextGoGoGo.setOnClickListener(this)
        binding.imageViewNextGoGoGo.setOnClickListener(this)
        binding.imageViewBack.setOnClickListener(this)
        mModel.setVisibleAdver(false)
        changeGirlFoto(mSeekBar.progress)

        return myView
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val s:String= mSeekBar.progress.toString()
        binding.textViewNumberwords.setText(s)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        Log.d("asqs","1")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        val s:String= mSeekBar.progress.toString()
        binding.textViewNumberwords.setText(s)
        changeGirlFoto(mSeekBar.progress)
    }

    private fun changeGirlFoto(progress:Int){
        if(progress>SAD_GIRL_NUMBER){
            binding.imageView6.setImageResource(R.drawable.ic_girl_sad)
        }else{
            binding.imageView6.setImageResource(R.drawable.ic_girl_happy)
        }

    }

    override fun onClick(v: View?) {
        if (v==binding.imageViewBack){
            mModel.setNextFragmentName(FragmentCreateCourseA())
        }else if (!mFlagClicButton) {
            val numberWords:String= binding.textViewNumberwords.text as String
            if (!numberWords.equals("0")) {


                InnerData.saveInt(CURENT_NUMBER_WARDS,numberWords.toInt())
                mModel.setNextFragmentName(FragmentChooseTheme())


                mFlagClicButton=true
            }else{
                Toast.makeText(context,R.string.course6,Toast.LENGTH_LONG).show()
            }
        }
    }
}