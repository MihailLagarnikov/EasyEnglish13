package ru.lagarnikov.easyenglish13

import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.View.*

val THEME_CURENT="theme_curent"
val TRAVEL_RU="Путешествия"
val FAMALI_RU="Семья"
val FEED_RU="Еда"
val FACE_RU="Внешность"
val TRAVEL_EN="travel"
val FAMALI_EN="famili"
val FEED_EN="feed"
val FACE_EN="face"

val SPEED_SPEACH_FAST:Float=0.7F
val SPEED_SPEACH_LOW:Float=0.13F
val CURENT_NUMBER_WARDS="curent_wards"
val LESSON_CREATED="lesson created"
val LESSON_NUMBER="lesson number"

//DB
val VERSION_DB=1
val COLUMN_ID="_id"
val COLUMN_EN="en"
val COLUMN_RU="ru"
val COLUMN_DONE="done"//показывает что слово изученно или нет (1 или 0), 1 - если все уроки изученны
val COLUMN_LESSON_A_DONE="lessonA"
val COLUMN_LESSON_B_DONE="lessonB"
val COLUMN_LESSON_C_DONE="lessonC"
val COLUMN_LESSON_D_DONE="lessonD"
val COLUMN_DONE_PATH="done_path"//показывает процент изученности
val COLUMN_ERROE_SUMM="errorSum"//Сумарное количество ошибок по слову
val COLUMN_ERROE_LESSON_A="errorLessonA"
val COLUMN_ERROE_LESSON_B="errorLessonB"
val COLUMN_ERROE_LESSON_C="errorLessonC"
val COLUMN_ERROE_LESSON_D="errorLessonD"
val COLUMN_STATE="state"
val STATE_NOT_STUDIED=0
val STATE__STUDIED_LONG=1
val STATE__STUDIED_JUST=2
val STATE__STUD_NOW=3
val SATE_DELETED=4
val SATE_HARD_WARD=5
val DB_FALSE=0
val DB_TRUE=1
val VOICE_RECOGNITION_REQUEST_CODE = 1234
val SEEKBAR_NORM = 10

val TIMER_MAX:Long=3000
val TIMER_MAX_TEXT:String="3"
val TIMER_INTERVAL:Long=1000


enum class TypeTest{
    TestA,TestB,TestC,TestD, TestE
}
val NUMBER_TYPE_TEST=4

fun getTableName(themeName:String):String{
    when(themeName){
        TRAVEL_RU -> return TRAVEL_EN
        FAMALI_RU -> return FAMALI_EN
        FEED_RU -> return FEED_EN
        FACE_RU -> return FACE_EN
        else -> return TRAVEL_EN
    }
}

fun getBoleanWithIntDb(i:Int):Boolean{
    if (i== DB_FALSE){
        return false
    }
    return true
}

fun getTypTest(i:Int, presenter: MyLessonPresenter):Fragment{
    when(i){
        1 ->return TestA(presenter)
        2 ->return TestB(presenter)
        3 ->return TestC(presenter)
        4 ->return TestD(presenter)
        5 ->return TestE(presenter)
    }
    return TestA(presenter)
}

/*
fun getTypTestInFragmentName(i:TypeTest):ContractFragmentCallActivity.FragmentName{
    when(i){
        TypeTest.TestA ->return ContractFragmentCallActivity.FragmentName.LessonA
        TypeTest.TestB ->return ContractFragmentCallActivity.FragmentName.LessonB
        TypeTest.TestC ->return ContractFragmentCallActivity.FragmentName.LessonC
        TypeTest.TestD ->return ContractFragmentCallActivity.FragmentName.LessonD
    }
}
*/




