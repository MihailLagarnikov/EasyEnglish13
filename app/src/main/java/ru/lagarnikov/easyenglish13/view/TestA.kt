package ru.lagarnikov.easyenglish13.view

import android.os.Bundle
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.model.MyLessonPresenter
import ru.lagarnikov.easyenglish13.databinding.FragmentMidleTestABBinding
import android.app.Activity
import android.view.inputmethod.InputMethodManager


open class TestA():Fragment(),View.OnClickListener {
    private lateinit var  mPresenter:MyLessonPresenter
    protected val mTypeTest=getTypeTest()
    protected var mAnswerNotDone:Boolean=false
    private var mAnswerTrue:Boolean=false
    private var mDataVisibile=getDataVisibileVIew()
    protected lateinit var mCurentDataTest:DataMidleFragment
    lateinit var binding: FragmentMidleTestABBinding
    lateinit var mModel: MyViewModel

    protected open fun getTypeTest():TypeTest{
        return TypeTest.TestA
    }

    protected open fun getDataVisibileVIew():DataVisibileView{
        return DataVisibileView(mAnswerNotDone,mAnswerTrue,true,0)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_midle_test_a_b,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)

        val myView=binding.root
        initModelAndPresenter()
        binding.mData=mCurentDataTest
        binding.mVisibile= mDataVisibile
        mModel.setVisibleElements(mDataVisibile)
        mModel.setVisibleTopFragment(true)
        binding.card1.setOnClickListener(this)
        binding.card2.setOnClickListener(this)
        binding.card3.setOnClickListener(this)
        binding.card4.setOnClickListener(this)
        binding.imageViewSound1TestAB.setOnClickListener(this)
        binding.imageViewSound2TestAB.setOnClickListener(this)
        binding.imageViewSound3TestAB.setOnClickListener(this)
        binding.imageViewSound4TestAB.setOnClickListener(this)
        binding.imageSaundMainTestAB.setOnClickListener(this)
        startSpeachTestB()
        setDataTopFr()
        try {
            val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.getCurrentFocus().getWindowToken(), 0)
        } catch (e: Exception) {
            //ошибочка и ни чего)
        }



        mModel.setVisibleAdver(true)


        return myView
    }


    private fun initModelAndPresenter(){
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)
        mPresenter=mModel.mPresenter
        mCurentDataTest =mPresenter.getTestDate(mTypeTest)
    }

    private fun chitTest(){
        mDataVisibile.answerTrue=true
        answerDone()
    }

    private fun setDataTopFr(){
        try {
            val dataTop: DataTopFragment = mModel.getDataTopFragment().value!!
            dataTop.typeTest=TypeTest.TestA
            mModel.setDataTopFragment(dataTop)
        } catch (e: Exception) {
            //значит ыерхний фрагмент еще не созданн, ни чего страшного)
            val s=e.toString()
            val d=0
        }
    }

    protected open fun startSpeachTestB(){

    }

    override fun onClick(v: View?) {
        if (!mAnswerNotDone) {
            when(v){
                binding.card1 -> {mDataVisibile.answerTrue=mCurentDataTest.textTrue
                                  mDataVisibile.numberAnswer=1
                answerDone()}
                binding.card2 -> {mDataVisibile.answerTrue=mCurentDataTest.text2True
                    mDataVisibile.numberAnswer=2
                answerDone()}
                binding.card3 -> {mDataVisibile.answerTrue=mCurentDataTest.text3True
                    mDataVisibile.numberAnswer=3
                answerDone()}
                binding.card4 -> {mDataVisibile.answerTrue=mCurentDataTest.text4True
                    mDataVisibile.numberAnswer=4
                answerDone()}
            }


        } else {
            when(v){
                binding.imageViewSound1TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text1)
                binding.imageViewSound2TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text2)
                binding.imageViewSound3TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text3)
                binding.imageViewSound4TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text4)
            }

        }
    }

    private fun answerDone(){
        mModel.setNextWordSpeach(mCurentDataTest.titleEn)
        mAnswerNotDone=true
        mDataVisibile.answerNotDone=true
        mModel.setVisibleElements(mDataVisibile)
        mModel.mPresenter.testDone(mTypeTest,mDataVisibile.answerTrue)
    }
}