package ru.lagarnikov.easyenglish13.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.databinding.FragmentMidleTestEBinding

@SuppressLint("ValidFragment")
open class TestE @SuppressLint("ValidFragment") constructor
    (open val mPresenter: MyLessonPresenter): Fragment(), View.OnClickListener {
    protected val mTypeTest=getTypeTest()
    private var mAnswerNotDone:Boolean=false
    private var mAnswerTrue:Boolean=false
    protected var mDataVisibile=getDataVisibileVIew()
    lateinit var binding: FragmentMidleTestEBinding
    lateinit var mModel: MyViewModel
    protected val mCurentDataTest=mPresenter.getTestDate(mTypeTest)
    var mError=0;
    private var mReqestStop=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_midle_test_e,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(true)

        val myView=binding.root
        binding.mData=mCurentDataTest
        binding.mVisibile= mDataVisibile
        mModel.setVisibleElements(mDataVisibile)
        binding.textViewNextTectE.setOnClickListener(this)
        binding.imageView4.setOnClickListener(this)
        setDataTopFr()
        createObservUserSpeak()



        return myView
    }

    protected open fun getTypeTest(): TypeTest {
        return TypeTest.TestE
    }
    private fun createObservUserSpeak(){
        val lostSpeak:String= mModel?.getWordWhatUserSpeak()?.value ?: ""
        val speak=Observer<String>{newString ->
            if (!lostSpeak.equals(newString)) {
                binding.textViewOption1.setText(newString)
                chaecAnswer(newString)
            }
        }
        mModel.getWordWhatUserSpeak().observe(this,speak)

    }

    private fun setDataTopFr(){
        try {
            val dataTop:DataTopFragment= mModel.getDataTopFragment().value!!
            dataTop.typeTest=TypeTest.TestE
            mModel.setDataTopFragment(dataTop)
        } catch (e: Exception) {
            //значит верхний фрагмент еще не созданн, ни чего страшного)
        }
    }

    protected open fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(mAnswerNotDone,mAnswerTrue,false,0)
    }

    override fun onClick(v: View?) {

        when(v){
            binding.textViewNextTectE -> answerDone(false)
            binding.imageView4 -> callMicrophone()

        }
    }

    private fun chaecAnswer(text:String){
        if (text.equals(mCurentDataTest.titleEn)){
            answerDone(true)
        }else{
            answerDone(false)
        }
    }

    private fun answerDone(succes:Boolean){
        if (!mAnswerNotDone) {
            mAnswerNotDone=true
            mDataVisibile.answerNotDone=true
            mDataVisibile.answerTrue=succes
            mModel.setVisibleElements(mDataVisibile)
            mModel.mPresenter.testDone(TypeTest.TestE,mDataVisibile.answerTrue)
        }
    }

    protected fun callMicrophone(){
        val speach=SpeechRecognitionHelper()
        speach.run(this!!.activity!!,resources.getString(R.string.testE2))

    }
}