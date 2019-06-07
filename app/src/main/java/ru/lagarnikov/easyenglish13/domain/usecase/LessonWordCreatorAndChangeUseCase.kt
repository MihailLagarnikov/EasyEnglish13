package ru.lagarnikov.easyenglish13.domain.usecase

import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.domain.*
import ru.lagarnikov.easyenglish13.presentation.IS_THEME_FINISH_CHILD
import java.util.ArrayList

class LessonWordCreatorAndChangeUseCase {
    private var count=0
    private var isWordsInThemeFinished=false

    //Возвращает неизученные слова для нового урока
    fun getNewListWordsLesson(oldList: ArrayList<DataSql>, themeName:String):ArrayList<DataSql> {
        val maxWords= MAX_WORD
        var numberWords=0

        for (n in 0 until  oldList.size){
            if (oldList.get(n).state == STATE_NOT_STUDIED && numberWords < maxWords){
                oldList.get(n).state= STATE__STUD_NOW
                numberWords++
            }
        }

        if (numberWords == 0){
            //значит закончились слов втеме, и урок заканчивается
            isWordsInThemeFinished=true
            InnerData.saveBoolean(IS_THEME_FINISH_CHILD + themeName,true )
        }
        return oldList

    }

    internal fun isFinishWordsInTheme():Boolean{

        return isWordsInThemeFinished
    }

    internal fun startLesson(){
        isWordsInThemeFinished=false
    }


    //После того как урок пройден изменяем статус слов с STATE__STUDIED_JUST на STATE__STUDIED_LONG
    internal fun changeItemStateAfterLesson(mListItem:ArrayList<DataSql>):ArrayList<DataSql>{
        count=0

        for (i in 0 until mListItem.size){

            if (mListItem.get(i).state== STATE__STUDIED_JUST){
                count++

                mListItem.get(i).state= STATE__STUDIED_LONG
            }else  if (mListItem.get(i).state== STATE__STUD_NOW){
                mListItem.get(i).state = STATE_NOT_STUDIED
            }
        }
        return mListItem
    }

    //Если тема изучается еще раз - это сбрасывает результат
    internal fun createListIfstudyAgain(mListItem:ArrayList<DataSql>):ArrayList<DataSql>{

        for (i in 0 until mListItem.size){
            mListItem.get(i).state = STATE_NOT_STUDIED
        }
        return mListItem
    }

    internal fun getCountNewWords():Int{
        return count
    }
}