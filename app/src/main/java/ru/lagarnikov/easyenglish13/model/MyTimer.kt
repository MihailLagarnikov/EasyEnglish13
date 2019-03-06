package ru.lagarnikov.easyenglish13.model

import ru.lagarnikov.easyenglish13.InnerData
import ru.lagarnikov.easyenglish13.TIMER_All
import ru.lagarnikov.easyenglish13.TIMER_LESSON
import ru.lagarnikov.easyenglish13.TIMER_Theme
import java.util.*

class MyTimer(var mThemeName:String) {
    private var mTimer:Timer=Timer()

    fun timerStart(){
        mTimer.schedule(MyTimerTask(mThemeName),0,1000*60)
    }

    fun timerStop(){
        mTimer.cancel()
    }
    class MyTimerTask( val mThemeName:String):TimerTask(){
        override fun run() {

            val timLesson=InnerData.loadInt(TIMER_LESSON)+1
            val timTheme=InnerData.loadInt(TIMER_Theme + mThemeName)+1
            val timAll=InnerData.loadInt(TIMER_All)+1

            InnerData.saveInt(TIMER_LESSON,timLesson)
            InnerData.saveInt(TIMER_Theme + mThemeName,timTheme)
            InnerData.saveInt(TIMER_All,timAll)

        }

    }
}