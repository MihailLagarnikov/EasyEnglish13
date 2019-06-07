package ru.lagarnikov.easyenglish13

import org.junit.Assert
import org.junit.Test
import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.domain.MAX_WORD
import ru.lagarnikov.easyenglish13.domain.STATE_NOT_STUDIED
import ru.lagarnikov.easyenglish13.domain.STATE__STUD_NOW
import ru.lagarnikov.easyenglish13.domain.usecase.LessonWordCreatorAndChangeUseCase

class LessonWordCreatorAndChangeUseCaseTest {
    private val mLesWordCr= LessonWordCreatorAndChangeUseCase()
    private fun getListDataSql(sizeList:Int,numberWordsWithStateStudyNow:Int):ArrayList<DataSql>{
        val newList= arrayListOf<DataSql>()
        var ss=0
        for (i in 0 until sizeList){
            ss++
            val state= if (ss <= numberWordsWithStateStudyNow){
                STATE__STUD_NOW
            }else{
                STATE_NOT_STUDIED
            }
            newList.add(DataSql(i,"","",0,0,0,0,0
                ,0,0,0,0,state,""))
        }
        return newList
    }


private fun checkSizeList_getNewListWordsLesson(sizeList:Int,numberWordsWithStateStudyNow:Int, expectedRezult:Int){
        Assert
            .assertEquals(expectedRezult,mLesWordCr.getNewListWordsLesson(getListDataSql(sizeList,numberWordsWithStateStudyNow)
                ,"").size)

    }

    @Test
 fun runCheckSizeList(){
        checkSizeList_getNewListWordsLesson(200,0, MAX_WORD)
        checkSizeList_getNewListWordsLesson(200,190, MAX_WORD)
        checkSizeList_getNewListWordsLesson(20,0, MAX_WORD)
        checkSizeList_getNewListWordsLesson(20,1, MAX_WORD)
        checkSizeList_getNewListWordsLesson(20,10, MAX_WORD)
        checkSizeList_getNewListWordsLesson(10,0, MAX_WORD)
        checkSizeList_getNewListWordsLesson(9,0, 9)
        checkSizeList_getNewListWordsLesson(5,0, 5)
        checkSizeList_getNewListWordsLesson(1,0, 1)
        checkSizeList_getNewListWordsLesson(0,0, 0)
        checkSizeList_getNewListWordsLesson(20,11, 9)
        checkSizeList_getNewListWordsLesson(20,19, 1)
        checkSizeList_getNewListWordsLesson(20,20, 0)
    }
}