package ru.lagarnikov.easyenglish13.domain

import ru.lagarnikov.easyenglish13.domain.model.DataMidleFragment
import ru.lagarnikov.easyenglish13.presentation.FragmentName

interface UiPresenter {

    interface NavigationFragment{
        fun setNextFragmentName(fragmentName: FragmentName, isNeedAddToBackStack:Boolean)
    }

    interface ViewModelTestFragments{
        fun setTestDate(testData: DataMidleFragment)
        fun getTestDate(): DataMidleFragment
        fun setCurentProgress(curentProgress:Int)
        fun setNextWordSpeech(newString: String)
        fun answerDone(fragmentName: FragmentName,success:Boolean)
        fun timerFinish()
        fun exitLesson()
    }

    interface ViewModelStartScreen{
        fun setCountAllNewWord(newWord: Int)
        fun setCountThemeNewWord(newWord: Int,themeName:String)
        fun lessonFinish()
        fun startLesson(themeName: String)
        fun repitLesson(themeName: String)
    }

}