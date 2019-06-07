package ru.lagarnikov.easyenglish13.domain.interactor

import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.domain.Repositoriy
import ru.lagarnikov.easyenglish13.domain.STATE_NOT_STUDIED
import ru.lagarnikov.easyenglish13.domain.STATE__STUD_NOW
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.domain.model.DataQuestion
import ru.lagarnikov.easyenglish13.domain.usecase.*
import ru.lagarnikov.easyenglish13.presentation.*
import java.util.*

class TestFragmentInteractor(val repositoriy: Repositoriy
                             , val uiViewModelTestFragment:UiPresenter.ViewModelTestFragments
,val uiViewModelStartScreen: UiPresenter.ViewModelStartScreen) {
    private var mThemeName=""
    private var positionId=0
    private val mTimer=TimerUseCase()
    private val mBaseUseCase=BaseUseCase(repositoriy)
    private val mMidlFragmentDataCreator=MidlFragmentDataCreatorUseCase()
    private val mLesonWordCreator=LessonWordCreatorAndChangeUseCase()
    private val mRandomTestFragmentAndDataUseCase=RandomTestFragmentAndDataUseCase()
    lateinit var mListItem: ArrayList<DataSql> // это копия базы SQL на выбранную тему, с ней работает класс
    private var isRepitLeson = false

    internal fun startLesson(themeName:String):DataQuestion{
        mThemeName=themeName
        checkThemeAgainStudy()
        mLesonWordCreator.startLesson()
        mListItem=mLesonWordCreator.getNewListWordsLesson(mBaseUseCase.getListDataTest(themeName), themeName)
        mBaseUseCase.insertDataListTest(mListItem,themeName)
        mTimer.startTimer(MAX_TIMER_LESSON_SECONDS,uiViewModelTestFragment,getStartTime())
        return getNextTesFragment()
    }

    internal fun repitLesson(themeName: String):DataQuestion {
        mThemeName=themeName
        isRepitLeson = true
        if (!checkWhatWeHaveWordsForRepitLesson(mBaseUseCase.getListDataTest(themeName))) {
            mBaseUseCase.reLoadAllDataDB(mThemeName)
            InnerData.saveInt(CURENT_WORDS_THEME_CHILD + mThemeName,0)
        }
        return startLesson(mThemeName)
    }

    private fun checkThemeAgainStudy(){
        if (InnerData.loadBoolean(IS_THEME_FINISH_CHILD + mThemeName) && !isRepitLeson){
            mBaseUseCase.reLoadAllDataDB(mThemeName)
            InnerData.saveBoolean(IS_THEME_FINISH_CHILD + mThemeName,false)
            InnerData.saveInt(CURENT_WORDS_THEME_CHILD + mThemeName,0)

        }
        isRepitLeson=false
    }

    internal fun getNextTesFragment(): DataQuestion {
        if (isLessonFinish()) {
            return ifLessonFinish()
        }

        if (checkWhatWeHaveWords(mListItem)) {
            return createTestFragmentAndData()
        } else {
            mListItem = mLesonWordCreator.getNewListWordsLesson(mBaseUseCase.getListDataTest(mThemeName), mThemeName)
            return getNextTesFragment()
        }

    }

    private fun isLessonFinish():Boolean{
        return  (mLesonWordCreator.isFinishWordsInTheme() || mTimer.isTimeFinish())
    }

    private fun ifLessonFinish():DataQuestion{
            mTimer.disposTimer()
            setChangeInListData()
            return DataQuestion(fragmentName = FragmentName.StartFragment,isLessonFinish = true)

    }
    private fun setChangeInListData(){
        mListItem=mLesonWordCreator.changeItemStateAfterLesson(mListItem)
        mBaseUseCase.insertDataListTest(mListItem,mThemeName)
        uiViewModelStartScreen.setCountThemeNewWord(mLesonWordCreator.getCountNewWords(),mThemeName)
    }

    private fun getStartTime():Int{
        if (InnerData.loadBoolean(IS_PAUSE_DID)){
            InnerData.saveBoolean(IS_PAUSE_DID,false)
            return InnerData.loadInt(TIMER_CURENT_SEC)
        }else{
            return 0
        }
    }
     fun pause(){
        mTimer.disposTimer()
        InnerData.saveBoolean(IS_PAUSE_DID,true)
        InnerData.saveInt(TIMER_CURENT_SEC,mTimer.getCurentTime())
    }

    fun resume(){
        if (InnerData.loadBoolean(IS_PAUSE_DID)){
            mTimer.startTimer(MAX_TIMER_LESSON_SECONDS,uiViewModelTestFragment,
                InnerData.loadInt(TIMER_CURENT_SEC))
            InnerData.saveBoolean(IS_PAUSE_DID,false)
        }

    }

    fun destroy(){
        setChangeInListData()
        mTimer.disposTimer()
    }

    private fun createTestFragmentAndData():DataQuestion{
        val fragmentData=mRandomTestFragmentAndDataUseCase.getFragmentTestType(mListItem)
        positionId= fragmentData.nextEnWord.id!!
        val dataOption=mMidlFragmentDataCreator.getTestDate(fragmentData,mListItem)
        return DataQuestion(dataOption,fragmentData.nextFragmentName)
    }

    fun exitLesson(){
        mTimer.disposTimer()
        setChangeInListData()
    }

     fun answerDone(fragmentName: FragmentName, success: Boolean):Boolean {
       if (success){
           mListItem=UserAnswerRegistrUseCase().writeAnswerInList(mListItem,positionId - 1,fragmentName,mThemeName)
           mBaseUseCase.insertDataListTest(mListItem,mThemeName)
       }
         return true
    }

    private fun checkWhatWeHaveWords(list:ArrayList<DataSql>):Boolean{
        for(data in list){
            if (data.state == STATE__STUD_NOW){
                return true
            }
        }
        return false
    }

    private fun checkWhatWeHaveWordsForRepitLesson(list:ArrayList<DataSql>):Boolean{
        for(data in list){
            if (data.state == STATE_NOT_STUDIED){
                return true
            }
        }
        return false
    }
}