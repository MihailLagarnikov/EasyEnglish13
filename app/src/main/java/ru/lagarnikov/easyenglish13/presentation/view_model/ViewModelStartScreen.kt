package ru.lagarnikov.easyenglish13.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.data.repositoriy.TestDataRepositoriy
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.domain.interactor.RxIntertor
import ru.lagarnikov.easyenglish13.domain.model.DataQuestion
import ru.lagarnikov.easyenglish13.presentation.*

class ViewModelStartScreen: ViewModel(),UiPresenter.ViewModelStartScreen {
    var mThemeName= InnerData.loadText(THEME_NAME)
    var isLessonDone=false
    private val isBackgroundWork= MutableLiveData<Boolean>()

    val mNavigationPresentor=NavigationPresentor()
    val mViewModelTestFragment=ViewModelTestFragment(this)
    val mReposotoriy=TestDataRepositoriy.instanse
    val mRxIntertor=RxIntertor(mReposotoriy
        ,mViewModelTestFragment
        ,mViewModelTestFragment.mViewModelStartScreen)

    override fun setCountAllNewWord(newWord: Int) {
       val mCountAllWard=newWord + InnerData.loadInt(COUNT_ALL_WORD)
        InnerData.saveInt(COUNT_ALL_WORD,mCountAllWard)
    }

    override fun setCountThemeNewWord(newWord: Int, themeName: String) {
        val mCountThemeWard=newWord + InnerData.loadInt(CURENT_WORDS_THEME_CHILD + themeName)
        InnerData.saveInt(CURENT_WORDS_THEME_CHILD + themeName,mCountThemeWard)
    }

    fun getCountAllWord():Int{
        return InnerData.loadInt(COUNT_ALL_WORD)
    }

    fun getCountThemeWord(themeName: String):Int{
        return InnerData.loadInt(CURENT_WORDS_THEME_CHILD + themeName)
    }

    fun setResource(res:Resources){
        mReposotoriy.setResourse(res)
    }

    internal fun setThemeName(themeName: String){
        mThemeName=themeName
        mViewModelTestFragment.mThemeName=themeName

    }

    override fun lessonFinish() {
        isLessonDone=true
    }

    override fun startLesson(themeName: String) {
        setThemeName(themeName)
        mRxIntertor.startLesson(themeName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsBackgroundWork(true)}
            .subscribe { data -> domainAnswer(data) }
    }

    override fun repitLesson(themeName: String) {
        setThemeName(themeName)
        mRxIntertor.repitLesson(themeName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setIsBackgroundWork(true)}
            .subscribe { data -> domainAnswer(data) }
    }

    fun domainAnswer(dataQuestion: DataQuestion){
        if (!dataQuestion.isLessonFinish){
            mViewModelTestFragment.setTestDate(dataQuestion.dataMidleFragment)
        }
        isLessonDone=dataQuestion.isLessonFinish
        mNavigationPresentor.setNextFragmentName(dataQuestion.fragmentName,false)

    }

    fun getIsBackgroundWork(): LiveData<Boolean> {
        return isBackgroundWork
    }

    fun setIsBackgroundWork(newBoolean: Boolean){
        isBackgroundWork.value=newBoolean
    }
}