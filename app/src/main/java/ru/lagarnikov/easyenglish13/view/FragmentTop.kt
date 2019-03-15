package ru.lagarnikov.easyenglish13.view

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.model.MyLessonPresenter
import ru.lagarnikov.easyenglish13.databinding.FragmentTopBinding
import android.content.DialogInterface
import android.widget.FrameLayout
import ru.lagarnikov.easyenglish13.getAllThemeData
import ru.lagarnikov.easyenglish13.getOrangeFotoTopFragment
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.DisplayMetrics




class FragmentTop():Fragment(),View.OnClickListener {
    private lateinit var  mPresenter:MyLessonPresenter
    private lateinit var mVisibile:DataVisibileView;
    lateinit var mModel: MyViewModel
    private lateinit var mDataTop:DataTopFragment
    private lateinit var mTimer:CountDownTimer
    private var mTimerTIme:Long= TIMER_MAX
    private var mTimerPause:Boolean=false
    lateinit var binding: FragmentTopBinding
    private var mMinimumSize=false
    private var mMaxSize=InnerData.loadInt(MAX_SIZE_FRAGMENT_TOP)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container,
            false)
        val myView=binding.root
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)
        mPresenter=mModel.mPresenter
        createObserverVisibleView()
        binding.mVisibile=mVisibile
        mDataTop=mModel.mPresenter.getTopFragmentDate()
        mModel.setDataTopFragment(mDataTop)
        binding.mData=mDataTop
        binding.mMinimizeSize=mMinimumSize

        binding.constrFragmentTop.setOnClickListener(this)
        binding.imageView2.setOnClickListener(this)
        binding.imageViewMinimaizer.setOnClickListener(this)
        createProgressLine()
        createObserverDataTop()
        setBackgroundImage()
        createOrLoadSize()

        return myView
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
    fun refreshData(){
        mDataTop=mModel.mPresenter.getTopFragmentDate()
        mModel.setDataTopFragment(mDataTop)
        binding.mData=mDataTop
        setBackgroundImage()
    }

    private fun setBackgroundImage(){
        val data= getAllThemeData()
        for(i in 0 until data.size){
            val ss:String=resources.getString(data.get(i).themeName).toUpperCase()
            if (mDataTop.theneName.toUpperCase().equals(ss)){
                binding.imageCurentTheme.setImageResource(getOrangeFotoTopFragment().get(i))
            }
        }

    }

    private fun setMinimBackgroundImage(){
        val data= getAllThemeData()
        for(i in 0 until data.size){
            val ss:String=resources.getString(data.get(i).themeName).toUpperCase()
            if (mDataTop.theneName.toUpperCase().equals(ss)){
                binding.imageCurentTheme.setImageResource(getOrangeFotoTopFragmentMInimum().get(i))
            }
        }

    }

    private fun createObserverVisibleView(){

        val visible=Observer<DataVisibileView>{
            newData -> binding.mVisibile=newData
            mVisibile=newData
            if (mVisibile.answerNotDone){
                startTimer(TIMER_MAX)
            }
        }
        mModel.getVisibleElements().observe(this,visible)
        mVisibile=DataVisibileView(false,false,false,0)

    }

    private fun createObserverDataTop(){
        val data= Observer<DataTopFragment> {newData ->changeTextHint(newData.typeTest)
        createProgressLine()}
        mModel.getDataTopFragment().observe(this,data)
    }

    private fun changeTextHint(typeTest: TypeTest){
        when(typeTest){
            TypeTest.TestA,TypeTest.TestB ->binding.textViewHint.setText(resources.getString(R.string.hintTestTop1))
            TypeTest.TestC,TypeTest.TestD ->binding.textViewHint.setText(resources.getString(R.string.hintTestTop2))
            TypeTest.TestE ->binding.textViewHint.setText(resources.getString(R.string.hintTestTop3))
        }
    }

    fun startTimer(timerMax:Long){

        mTimer= object : CountDownTimer(timerMax, TIMER_INTERVAL) {


            override fun onTick(millisUntilFinished: Long) {
                val s:String= (millisUntilFinished / TIMER_INTERVAL).toString()
                mTimerTIme=millisUntilFinished
                mDataTop.countTimer=s
                binding.mData=mDataTop
            }

            override fun onFinish() {
                mDataTop.countTimer="0"
                mModel.setNextFragmentName(mModel.mPresenter.getFragmentTestType())
            }


        }.start()

    }

    override fun onClick(v: View?) {
        if (v==binding.constrFragmentTop) {
            if (mVisibile.answerNotDone){
                if (mTimerPause){
                    startTimer(mTimerTIme)
                    binding.textOunting.setVisibility(View.VISIBLE)
                    binding.imageView3.setVisibility(View.INVISIBLE)
                }else{
                    mTimer.cancel()
                    binding.textOunting.setVisibility(View.INVISIBLE)
                    binding.imageView3.setVisibility(View.VISIBLE)
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
        binding.textLessonNumber.visibility=vis
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

    private fun createDilogAboutExitLesson(){
        val dialog=AlertDialog.Builder(context)
        dialog.setMessage(resources.getString(R.string.dialog1))
        dialog.setPositiveButton(resources.getString(R.string.dialog2), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, arg1: Int) {
                //выход из урока
                mModel.stopTimer()
                mModel.mPresenter.exitInLesson()
                mModel.setNextFragmentName(FragmentThemeWord())
                InnerData.saveInt(STATUS_PROGRAMM, STATUS_3)
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
        var dda=mPresenter.getMaxProgress()
        if (dda==0){
            dda=1
        }
        var fss: Float
        try {
            val fss2:Float= (weigtScrin/mPresenter.getMaxProgress()).toFloat()
            fss = mPresenter.getCurentProgress().toFloat()*fss2
        } catch (e: ArithmeticException) {
            fss=1f
        }
        val dd:Int= fss.toInt()
         set.constrainWidth(R.id.imageViewProgressLineTop,dd)
         set.applyTo(mCOnstrProgress)


    }


    override fun onDestroy() {
        super.onDestroy()
        if (mTimer!=null){
            mTimer.cancel()
        }
    }
}