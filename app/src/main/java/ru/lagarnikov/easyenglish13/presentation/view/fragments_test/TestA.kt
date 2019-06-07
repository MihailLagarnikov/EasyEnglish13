package ru.lagarnikov.easyenglish13.presentation.view.fragments_test

import android.os.Bundle
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentMidleTestABBinding
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import ru.lagarnikov.easyenglish13.presentation.getFragmentTag
import ru.lagarnikov.easyenglish13.domain.model.DataMidleFragment
import ru.lagarnikov.easyenglish13.presentation.model.DataVisibileView
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelTestFragment


open class TestA():Fragment(),View.OnClickListener {
    protected val mTypeTest=getTypeTest()
    protected var mAnswerNotDone:Boolean=false
    private var mAnswerTrue:Boolean=false
    private var mDataVisibile=getDataVisibileVIew()
    protected lateinit var mCurentDataTest: DataMidleFragment
    lateinit var binding: FragmentMidleTestABBinding
    protected lateinit var mViewModelTest:ViewModelTestFragment

    protected open fun getTypeTest():FragmentName{
        return FragmentName.TestA
    }

    protected open fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(mAnswerNotDone, mAnswerTrue, true, 0)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_midle_test_a_b,
            container, false)

        val myView=binding.root
        initModelAndPresenter()
        changeCurentTestData()
        binding.mData=mCurentDataTest
        binding.mVisibile= mDataVisibile
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
        mViewModelTest.mViewModelStartScreen.setIsBackgroundWork(false)

        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisibleElementsTopFragment(false)
    }

    private fun initModelAndPresenter(){
        mViewModelTest=  ViewModelProviders.of(this!!.activity!!)
            .get(ViewModelStartScreen::class.java)
            .mViewModelTestFragment
        mCurentDataTest =mViewModelTest.getTestDate()
    }

    protected open fun changeCurentTestData(){

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
                binding.imageViewSound1TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text1)
                binding.imageViewSound2TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text2)
                binding.imageViewSound3TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text3)
                binding.imageViewSound4TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text4)
            }

        }
    }

    protected open fun answerDone(){
        mViewModelTest.setNextWordSpeech(mCurentDataTest.titleEn)
        mAnswerNotDone=true
        mDataVisibile.answerNotDone=true
        setVisibleElementsTopFragment(true)
        mViewModelTest.answerDone(mTypeTest,mDataVisibile.answerTrue)
    }

    private fun setVisibleElementsTopFragment(answerDone:Boolean){
        val fragmentTop= activity!!
            .supportFragmentManager
            .findFragmentByTag(getFragmentTag(FragmentName.FragmentTop))

        if (fragmentTop != null && fragmentTop is FragmentTop){
            fragmentTop.setVisibilyElementDependensOfAnswerDone(answerDone)
            fragmentTop.setThemeTopFr(mViewModelTest.mThemeName)
        }
    }
}