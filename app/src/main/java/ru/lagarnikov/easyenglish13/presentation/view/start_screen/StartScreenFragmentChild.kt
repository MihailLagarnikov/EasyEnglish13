package ru.lagarnikov.easyenglish13.presentation.view.start_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.FragmentStartChildThemeBinding
import ru.lagarnikov.easyenglish13.presentation.*
import ru.lagarnikov.easyenglish13.presentation.view.theme_screen.DialogThemeWords
import android.view.View.MeasureSpec
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen


class StartScreenFragmentChild:Fragment(),View.OnClickListener {
    private lateinit var binding:FragmentStartChildThemeBinding
    private val sDecelerator = DecelerateInterpolator()
    private val sOvershooter = OvershootInterpolator(10f)
    private lateinit var mAgument:Bundle
    private var mThemeName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mAgument= this.arguments ?: Bundle()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_start_child_theme,
            container, false)
        val myView=binding.root
        animClickBtn(binding.buttonRepitTheme)
        animClickBtn(binding.buttonStartStudi)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStartStudi.setOnClickListener(this)
        binding.buttonRepitTheme.setOnClickListener(this)

        //четыре view ниже при нажатии попадаешь на список слов темы
        binding.imageViewTheme.setOnClickListener(this)
        binding.textViewThemeName.setOnClickListener(this)
        binding.textView30.setOnClickListener(this)
        binding.imageView34.setOnClickListener(this)

        mThemeName=resources.getString(mAgument.getInt(THEME_NAME_CHILD))

        binding.imageViewTheme.setImageResource(mAgument.getInt(THEME_IMAGE_RES_CHILD))
        binding.textViewThemeName.setText(mThemeName.toUpperCase())
        val maxProgress=resources.getString(mAgument.getInt(MAX_WORDS_THEME_CHILD))
        val curentProgress=InnerData.loadInt(CURENT_WORDS_THEME_CHILD + mThemeName)
        binding.textViewMax.setText(maxProgress)

        binding.textViewCurentCountWords.setText(curentProgress.toString())
        isThemeFinish(InnerData.loadBoolean(IS_THEME_FINISH_CHILD + mThemeName ))
        createProgressLine(
            curentProgress
            ,maxProgress.toInt())
        doInvisiblProgressIfNotStudy(InnerData.loadInt(CURENT_WORDS_THEME_CHILD + mThemeName))
    }



    private fun isThemeFinish(isThemeFinish:Boolean){
        var visib=View.VISIBLE
        var visib2=View.VISIBLE
        var visib3=View.VISIBLE
        if (isThemeFinish){
            visib=View.GONE
            visib2=View.VISIBLE
            visib3=View.VISIBLE

            binding.textView24.setText(resources.getString(R.string.startChild6))
            binding.buttonStartStudi.setText(resources.getString(R.string.startChild5))
        }else{
            visib=View.VISIBLE
            visib2=View.GONE
            visib3=View.INVISIBLE

            binding.textView24.setText(resources.getString(R.string.startChild1))
            binding.buttonStartStudi.setText(resources.getString(R.string.startChild4))
        }

        binding.textViewMax.visibility=visib
        binding.textViewCurentCountWords.visibility=visib
        binding.textView26.visibility=visib
        binding.imageViewRectanglProgress.visibility=visib

        binding.buttonRepitTheme.visibility=visib3
        binding.imageViewRepiIcon.visibility=visib2

    }

    override fun onClick(v: View?) {
        when(v){
            binding.buttonStartStudi -> pressButtomStartStudy()
            binding.buttonRepitTheme -> pressButtonRepitTheme()
            binding.imageViewTheme
                , binding.textViewThemeName
                , binding.textView30
                , binding.imageView34 -> pressThemeWords()
        }
    }
    private fun pressButtomStartStudy(){
        InnerData.saveInt(SAVE_POSITION_THEME,arguments!!.getInt(POSITION_THEME_CHILD))
        InnerData.saveText(THEME_NAME,mThemeName)
        callParentFragment(true,false)
    }

    private fun pressButtonRepitTheme(){
        InnerData.saveBoolean(IS_THEME_FINISH_CHILD + mAgument.getInt(THEME_NAME_CHILD),false )
        callParentFragment(false,true)
    }

    private fun callParentFragment(isStartLesson:Boolean, isRepitLesson:Boolean){
        val parentFragment=activity!!.supportFragmentManager.findFragmentByTag(getFragmentTag(FragmentName.StartFragment))

        if (parentFragment is StartScreenFragment){
            if (isStartLesson) {
                parentFragment.startLesson(mThemeName)
            }
            if(isRepitLesson){
                parentFragment.repitLesson(mThemeName)
            }

        }
    }

    private fun pressThemeWords(){
        ViewModelProviders.of(this.activity!!).get(ViewModelStartScreen::class.java).setIsBackgroundWork(true)
        val dialog= DialogThemeWords()
        val bundle=Bundle()
        bundle.putString(THEME_NAME_CHILD,mThemeName)
        dialog.arguments=bundle
        dialog.show(fragmentManager!!,"dialog")

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun animClickBtn(button: Button){
        button.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                button.animate().setInterpolator(sDecelerator).scaleX(.9f).scaleY(.9f)
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                button.animate().setInterpolator(sOvershooter).scaleX(1f).scaleY(1f)
            }
            return@OnTouchListener false

        })
    }

    //линия прогресса обучения и текст
    private fun createProgressLine(curentProgress:Int, maxProgress:Int){


        val mCOnstrProgress=binding.constrFragmentThemeChild
        val set= ConstraintSet()
        set.clone(mCOnstrProgress)

        binding.imageViewTheme.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val weigtScrin = binding.imageViewTheme.getMeasuredWidth()


        var fss: Float
        try {
            var fss2:Float= (weigtScrin.toFloat()/maxProgress.toFloat())
            fss = curentProgress.toFloat()*fss2

            if(curentProgress == maxProgress){
                fss=weigtScrin.toFloat()
            }

        } catch (e: ArithmeticException) {
            fss=1f
        }
        val dd:Int= fss.toInt()
        set.constrainWidth(R.id.imageView32,dd)
        set.applyTo(mCOnstrProgress)
    }

    private fun doInvisiblProgressIfNotStudy(curentProgress:Int){
        if (curentProgress == 0){
            binding.imageView32.visibility=View.INVISIBLE
        }
    }


}