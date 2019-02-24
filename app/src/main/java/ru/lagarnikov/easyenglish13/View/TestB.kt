package ru.lagarnikov.easyenglish13.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.DataVisibileView
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.TypeTest

@SuppressLint("ValidFragment")
class TestB @SuppressLint("ValidFragment") constructor
    (val mPresenter2: MyLessonPresenter):TestA(mPresenter2) {

    override fun getTypeTest(): TypeTest {
        return TypeTest.TestB
    }


    override fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(false,false,false,0)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v==binding.imageSaundMainTestAB){
            mModel.setNextWordSpeach(mCurentDataTest.titleEn)
        }
    }

    override fun startSpeachTestB() {
        mModel.setNextWordSpeach(mCurentDataTest.titleEn)
    }
}