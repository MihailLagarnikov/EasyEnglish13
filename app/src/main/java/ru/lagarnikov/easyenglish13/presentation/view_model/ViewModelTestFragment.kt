package ru.lagarnikov.easyenglish13.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lagarnikov.easyenglish13.domain.model.DataMidleFragment
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.domain.model.DataQuestion
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import ru.lagarnikov.easyenglish13.presentation.THEME_NAME

class ViewModelTestFragment(val mViewModelStartScreen: ViewModelStartScreen):UiPresenter.ViewModelTestFragments {
    var isTimerGo=false
    private lateinit var dataPresentor: DataQuestion
    private var mCurentProgress=0
    var mThemeName= InnerData.loadText(THEME_NAME)
    private lateinit var mTestDate: DataMidleFragment
    private val mNextSpeach= MutableLiveData<String>()
    override fun setTestDate(testData: DataMidleFragment) {
        mTestDate=testData
    }

    override fun getTestDate(): DataMidleFragment {
        return mTestDate
    }



    override fun setCurentProgress(curentProgress: Int) {
       mCurentProgress=curentProgress
    }

    fun getNextWordSpeech(): LiveData<String> {
        return mNextSpeach
    }

    override fun setNextWordSpeech(newString: String){
        mNextSpeach.value=newString
    }

    fun getCurentProgress():Int{
        return  mCurentProgress
    }

    override fun answerDone(fragmentName: FragmentName, success: Boolean) {
        mViewModelStartScreen.mRxIntertor.answerDone(fragmentName,success)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mViewModelStartScreen.setIsBackgroundWork(true)}
            .doAfterTerminate { mViewModelStartScreen.setIsBackgroundWork(false)}
            .subscribe { data -> getNextFragmentName() }
    }

    private fun getNextFragmentName(){
        mViewModelStartScreen.mRxIntertor.cerateNewQuestionData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mViewModelStartScreen.setIsBackgroundWork(true)}
            .doAfterTerminate { mViewModelStartScreen.setIsBackgroundWork(false)}
            .subscribe { data -> if (!isTimerGo) {
                mViewModelStartScreen.domainAnswer(data)
            }else{
                dataPresentor=data
            }
            }
    }

    override fun timerFinish() {
        if (!mViewModelStartScreen.getIsBackgroundWork().value!!){
            mViewModelStartScreen.domainAnswer(dataPresentor)
        }
    }

    override fun exitLesson() {
        mViewModelStartScreen.mRxIntertor.exitLesson()
        mViewModelStartScreen.mNavigationPresentor.setNextFragmentName(FragmentName.StartFragment,false)
    }
}