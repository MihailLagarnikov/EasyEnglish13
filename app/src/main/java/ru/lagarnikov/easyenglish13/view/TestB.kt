package ru.lagarnikov.easyenglish13.view

import android.view.View
import ru.lagarnikov.easyenglish13.DataVisibileView
import ru.lagarnikov.easyenglish13.TypeTest

class TestB():TestA() {

    override fun getTypeTest(): TypeTest {
        return TypeTest.TestB
    }


    override fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(false,false,false,0)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == binding.imageSaundMainTestAB) {
            mModel.setNextWordSpeach(mCurentDataTest.titleEn)
        }
        if (mAnswerNotDone) {
            when (v) {
                binding.imageViewSound1TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text1Answer)
                binding.imageViewSound2TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text2Answer)
                binding.imageViewSound3TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text3Answer)
                binding.imageViewSound4TestAB -> mModel.setNextWordSpeach(mCurentDataTest.text4Answer)
            }
        }
    }

    override fun startSpeachTestB() {
        mModel.setNextWordSpeach(mCurentDataTest.titleEn)
    }
}