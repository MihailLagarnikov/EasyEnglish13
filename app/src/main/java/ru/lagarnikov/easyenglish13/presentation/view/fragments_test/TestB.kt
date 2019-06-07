package ru.lagarnikov.easyenglish13.presentation.view.fragments_test

import android.view.View
import ru.lagarnikov.easyenglish13.presentation.model.DataVisibileView
import ru.lagarnikov.easyenglish13.presentation.FragmentName

open class TestB(): TestA() {

    override fun getTypeTest(): FragmentName{
        return FragmentName.TestB
    }


    override fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(false, false, false, 0)
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
        mViewModelTest.setNextWordSpeech(mCurentDataTest.titleEn)
    }
}