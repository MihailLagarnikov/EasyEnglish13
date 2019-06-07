package ru.lagarnikov.easyenglish13.presentation.view.fragments_test

import android.view.View
import ru.lagarnikov.easyenglish13.domain.model.DataMidleFragment
import ru.lagarnikov.easyenglish13.presentation.model.DataVisibileView
import ru.lagarnikov.easyenglish13.presentation.FragmentName

class TestF: TestB() {

    override fun getTypeTest(): FragmentName {
        return FragmentName.TestF
    }

    override fun getDataVisibileVIew(): DataVisibileView {
        return return DataVisibileView(false, false, true, 0)
    }

    override fun changeCurentTestData() {

        val newData= DataMidleFragment(
            mCurentDataTest.titleEn,
            mCurentDataTest.text1,
            mCurentDataTest.text2
            ,
            mCurentDataTest.text3,
            mCurentDataTest.text4,
            mCurentDataTest.text1Answer,
            mCurentDataTest.text2Answer
            ,
            mCurentDataTest.text3Answer,
            mCurentDataTest.text4Answer,
            mCurentDataTest.textTrue,
            mCurentDataTest.text2True
            ,
            mCurentDataTest.text3True,
            mCurentDataTest.text4True,
            mCurentDataTest.titleRu
        )
        mCurentDataTest=newData
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.imageSaundMainTestAB) {
            mViewModelTest.setNextWordSpeech(mCurentDataTest.titleEn)
        }
        if (mAnswerNotDone) {
            when (v) {
                binding.imageViewSound1TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text1Answer)
                binding.imageViewSound2TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text2Answer)
                binding.imageViewSound3TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text3Answer)
                binding.imageViewSound4TestAB -> mViewModelTest.setNextWordSpeech(mCurentDataTest.text4Answer)
            }
        }
    }

    override fun startSpeachTestB() {
    }

    override fun answerDone() {
        super.answerDone()
        mViewModelTest.setNextWordSpeech(mCurentDataTest.titleRu)
    }
}