package ru.lagarnikov.easyenglish13.presentation.view.fragments_test

import android.app.AlertDialog
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentTopBinding
import android.content.DialogInterface
import android.widget.FrameLayout
import ru.lagarnikov.easyenglish13.data.model.getAllThemeData
import android.util.DisplayMetrics
import ru.lagarnikov.easyenglish13.data.model.getOrangeFotoTopFragment
import ru.lagarnikov.easyenglish13.data.model.getOrangeFotoTopFragmentMInimum
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.presentation.*
import ru.lagarnikov.easyenglish13.presentation.model.DataTopFragment
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelTestFragment


class FragmentTop():Fragment(),View.OnClickListener {
    private lateinit var mDataTop: DataTopFragment
    private lateinit var mTimer:CountDownTimer
    private lateinit var mTimer2:CountDownTimer
    private var mTimerTIme:Long= TIMER_MAX
    private var mTimerPause:Boolean=false
    lateinit var binding: FragmentTopBinding
    private var mMinimumSize=false
    private var mMaxSize= InnerData.loadInt(MAX_SIZE_FRAGMENT_TOP)
    private lateinit var mViewModelTest: ViewModelTestFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container,
            false)
        val myView=binding.root
        mViewModelTest=  ViewModelProviders.of(this!!.activity!!)
            .get(ViewModelStartScreen::class.java)
            .mViewModelTestFragment

        binding.isAnswerNotDone=true
        binding.themeNeme=mViewModelTest.mThemeName.toUpperCase()
        binding.mMinimizeSize=mMinimumSize

        binding.constrFragmentTop.setOnClickListener(this)
        binding.imageView2.setOnClickListener(this)
        binding.imageViewMinimaizer.setOnClickListener(this)
        createProgressLine()
        setBackgroundImage()
        createOrLoadSize()
        startTimer2(TIMER_MAX_2)

        return myView
    }

    internal fun setThemeTopFr(themeName:String){
        binding.themeNeme=themeName
    }

    private fun createOrLoadSize(){
        if (mMaxSize==0){
            val params = binding.constrFragmentTop.getLayoutParams() as FrameLayout.LayoutParams
            mMaxSize=params.height
            InnerData.saveInt(MAX_SIZE_FRAGMENT_TOP,mMaxSize)

            if (smartfonSize()<=4.6){
                binding.imageViewMinimaizer.performClick()

            }
        }else{
            if (InnerData.loadBoolean(MIN_SIZE_PRESS)){
                binding.imageViewMinimaizer.performClick()
            }
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


    private fun setBackgroundImage(){
        val data= getAllThemeData()
        for(i in 0 until data.size){
            val ss:String=resources.getString(data.get(i).themeName).toUpperCase()
            if (mViewModelTest.mThemeName.toUpperCase().equals(ss)){
                binding.imageCurentTheme.setImageResource(getOrangeFotoTopFragment().get(i))
            }
        }

    }

    private fun setMinimBackgroundImage(){
        val data= getAllThemeData()
        for(i in 0 until data.size){
            val ss:String=resources.getString(data.get(i).themeName).toUpperCase()
            if (mViewModelTest.mThemeName.toUpperCase().equals(ss)){
                binding.imageCurentTheme.setImageResource(getOrangeFotoTopFragmentMInimum().get(i))
            }
        }

    }



    fun setVisibilyElementDependensOfAnswerDone(answerDone:Boolean){
       binding.isAnswerNotDone=!answerDone
        if (answerDone){
            startTimer(TIMER_MAX)
        }
    }



    override fun onClick(v: View?) {
        if (v==binding.constrFragmentTop) {
            if (!binding.isAnswerNotDone!!){
                if (mTimerPause){
                    startTimer(mTimerTIme)
                    binding.textOunting.setVisibility(View.VISIBLE)
                    binding.imageView3.setVisibility(View.INVISIBLE)
                    mViewModelTest.mViewModelStartScreen.mRxIntertor.resume()
                }else{
                    mTimer.cancel()
                    binding.textOunting.setVisibility(View.INVISIBLE)
                    binding.imageView3.setVisibility(View.VISIBLE)
                    mViewModelTest.mViewModelStartScreen.mRxIntertor.pause()
                }
                mTimerPause=!mTimerPause
            }
        }else if (v==binding.imageView2){
            //возможно пользователь хочет прервать курс... надо спросить его об этом

            createDilogAboutExitLesson()
        }else if(v==binding.imageViewMinimaizer){
            resizeFragmentTop()
        }
    }

    private fun resizeFragmentTop(){
        val vis:Int=if(mMinimumSize){View.VISIBLE}else{View.GONE}
        binding.textViewTheme.visibility=vis
        val params = binding.constrFragmentTop.getLayoutParams() as FrameLayout.LayoutParams

        if (mMinimumSize){
            setBackgroundImage()
            binding.imageViewMinimaizer.rotation=180f
            params.height=mMaxSize
        }else{
            binding.imageViewMinimaizer.rotation=0f
            setMinimBackgroundImage()
            params.height = (mMaxSize/2.5).toInt()
        }
        binding.constrFragmentTop.setLayoutParams(params)
        mMinimumSize=mMinimumSize.not()
        binding.mMinimizeSize=mMinimumSize
        InnerData.saveBoolean(MIN_SIZE_PRESS,mMinimumSize)
    }

    fun startTimer(timerMax:Long){
        mViewModelTest.isTimerGo=true

        mTimer= object : CountDownTimer(timerMax, TIMER_INTERVAL) {


            override fun onTick(millisUntilFinished: Long) {
                val s:String= (millisUntilFinished / TIMER_INTERVAL).toString()
                mTimerTIme=millisUntilFinished
                binding.countTimer=s
            }

            override fun onFinish() {
                binding.countTimer="0"
                mViewModelTest.isTimerGo=false
                mViewModelTest.timerFinish()
            }


        }.start()

    }

    fun startTimer2(timerMax:Long){
        mTimer2= object : CountDownTimer(timerMax, TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                createProgressLine()
            }

            override fun onFinish() {
                createProgressLine()
            }

        }.start()

    }

    private fun createDilogAboutExitLesson(){
        val dialog=AlertDialog.Builder(context)
        dialog.setMessage(resources.getString(R.string.dialog1))
        dialog.setPositiveButton(resources.getString(R.string.dialog2), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, arg1: Int) {
                //выход из урока
                mViewModelTest.exitLesson()
            }
        });
        dialog.setNegativeButton(resources.getString(R.string.dialog3),null)
        dialog.show()
    }


    //линия прогресса обучения и текст
    private fun createProgressLine(){

         val mCOnstrProgress=binding.constrFragmentTop
         val set= ConstraintSet()
         set.clone(mCOnstrProgress)
         val display = activity?.getWindowManager()?.getDefaultDisplay()
         val point= Point()
         display?.getSize(point)
         val weigtScrin=point.x

        var fss: Float
        try {
            val fss2:Float= (weigtScrin.toFloat()/(MAX_TIMER_LESSON_SECONDS).toFloat())
            fss = mViewModelTest.getCurentProgress().toFloat()*fss2

        } catch (e: ArithmeticException) {
            fss=1f
        }
        val dd:Int= fss.toInt()
         set.constrainWidth(R.id.imageViewProgressLineTop,dd)
         set.applyTo(mCOnstrProgress)


    }


    override fun onDestroy() {
        super.onDestroy()
        mViewModelTest.mViewModelStartScreen.mRxIntertor.destroy()
        if (mTimer!=null){
            mTimer.cancel()
        }
        if (mTimer2 != null){
            mTimer2.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        mViewModelTest.mViewModelStartScreen.mRxIntertor.pause()
    }

    override fun onResume() {
        super.onResume()
        mViewModelTest.mViewModelStartScreen.mRxIntertor.resume()
    }
}