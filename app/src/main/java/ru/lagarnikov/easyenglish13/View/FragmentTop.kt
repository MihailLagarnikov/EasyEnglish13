package ru.lagarnikov.easyenglish13.View

import android.annotation.SuppressLint
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
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.databinding.FragmentTopBinding

@SuppressLint("ValidFragment")
class FragmentTop @SuppressLint("ValidFragment") constructor
    (val mPresenter: MyLessonPresenter):Fragment(),View.OnClickListener {
    private lateinit var mVisibile:DataVisibileView;
    lateinit var mModel: MyViewModel
    private lateinit var mDataTop:DataTopFragment
    private lateinit var mTimer:CountDownTimer
    private var mTimerTIme:Long= TIMER_MAX
    private var mTimerPause:Boolean=false
    lateinit var binding: FragmentTopBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container,
            false)
        val myView=binding.root
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        createObserverVisibleView()
        binding.mVisibile=mVisibile
        mDataTop=mModel.mPresenter.getTopFragmentDate()
        binding.mData=mDataTop

        binding.constrFragmentTop.setOnClickListener(this)
        createProgressLine()
        createObserverDataTop()

        return myView
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
        val data= Observer<DataTopFragment> {newData ->changeTextHint(newData.typeTest) }
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
        }
    }


    //линия прогресса обучения и текст
    private fun createProgressLine(){
        val lineProgA=binding.imageViewProgressLineTop
        val lineProgB=binding.imageViewProgressLineBottom

         val mCOnstrProgress=binding.constrFragmentTop
         val set= ConstraintSet()
         set.clone(mCOnstrProgress)
         val display = activity?.getWindowManager()?.getDefaultDisplay()
         val point= Point()
         display?.getSize(point)
         val weigtScrin=point.x
         val curentPr=(weigtScrin  /mPresenter.getMaxProgress())*mPresenter.getCurentProgress()
         set.constrainWidth(R.id.imageViewProgressLineTop,curentPr)
         set.applyTo(mCOnstrProgress)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mTimer!=null){
            mTimer.cancel()
        }
    }
}