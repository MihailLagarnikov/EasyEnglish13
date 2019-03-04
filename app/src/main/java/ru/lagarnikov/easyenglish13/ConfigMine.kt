package ru.lagarnikov.easyenglish13

import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.View.*
import java.text.SimpleDateFormat
import java.util.*

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
val LESSON_REPIT_ALRM="lesson created repit"
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
val STATE__STUDIED_REPIT_1=6
val STATE__STUDIED_REPIT_2=7
val STATE__STUDIED_REPIT_NOW=8
val STATE__STUDIED_JUST=2
val STATE__STUD_NOW=3
val SATE_DELETED=4
val SATE_HARD_WARD=5
val DB_FALSE=0
val DB_TRUE=1
val VOICE_RECOGNITION_REQUEST_CODE = 1234
val SEEKBAR_NORM = 10
val CURENT_MAX_PROGRESS = "curMax_progr"


val NUMBER_WORDS_FOR_REPIT_LESSON = 2 //число слов в уроке повторении
val REPIT_LESSON_CREATED = "repitLessonCreated"

val TIMER_MAX:Long=3000
val TIMER_MAX_TEXT:String="3"
val TIMER_INTERVAL:Long=1000


enum class TypeTest{
    TestA,TestB,TestC,TestD, TestE
}
val NUMBER_TYPE_TEST_FULL=5 //сколько типов теста для слова в зависимости от типа курса, это первый курс
val NUMBER_TYPE_TEST_2=3
val NUMBER_TYPE_TEST_3=2


val CHOOSE_THEME_PROGRESS_PROCENT="choose_theme_progress_proc" // определяет(вместе с именем темы) имя шаредпрефа где храгится
// прогресс в процентах изучения для каждой темы
val CHOOSE_THEME_PROGRESS_WORD="progress_word" //сколько слов из темы изученно
val ALL_WORDS_DONE="all_words_done" //сколько слов из темы изученно
val CHOOSE_THEME_DONE="theme_done" //флаг что тема изученна или нет
val BOOLEAN_FLAG="boolFlag" //флаг что тема изученна или нет
val TIMER_LESSON="timer_lesson"
val TIMER_Theme="timer_theme"
val TIMER_All="timer_all"
val CURENT_COURSE="curent_course"
val CURENT_COURSE_1=1
val CURENT_COURSE_2=2
val CURENT_COURSE_3=3

val STATUS_PROGRAMM="status_programm"
val STATUS_1=1 // урок пройден, новый не начат
val STATUS_2=2 // мы начали новый урок, и пока еге не закончили
val STATUS_3=3 // мы вышли из урока в уровни выше, либо еще дошли до этого

val DONE_THEME_STATUS="doneThemeStatus" // когда тема сделанна, то в зависимости от пройденных контрольных она имеет статуы
val DONE_THEME_STATUS_0=0 // не пройденны контрольные еще
val DONE_THEME_STATUS_1=1 // 1 ая контрольная пройдена
val DONE_THEME_STATUS_2=2 // 1 ая контрольная пройдена
val DONE_THEME_STATUS_3=3 // 1 ая контрольная пройдена
val DONE_THEME_STATUS_4=4 // 1 ая контрольная пройдена
val DONE_THEME_STATUS_5=5 // 1 ая контрольная пройдена
val DONE_THEME_PERIOD="doneThemePeriod" // периуд между контрольными
val DONE_THEME_PERIOD_1=7 // дней
val DONE_THEME_PERIOD_2=14 // дней
val DONE_THEME_PERIOD_3=30 // дней
val DONE_THEME_PERIOD_4=90 // дней
val DONE_THEME_PERIOD_5=180 // дней


val MAX_WORD_CONTROLNAIA=30 //максимум слов в контрольной
val CONTROLNAIA_REZULT="controlRez"
val START_CONTROLNAIA="controlnaiStart" //создали контрольную
val CONTROLNAIA_ALARM="controlnaiALarm" //создали контрольную
val CONTROLNAIA_THEME="controlnaiTHEMe" //создали контрольную
val NUMBER_WORDS_IN_LESSON="numWordsInLesson" //создали контрольную
val DATA_LAST_LESSON="dataLastLesson" //датапоследнего задания




fun getPeriodBetwinControln(status:Int):Int{
    when(status){
        0 -> return 7
        1 ->return 14
        2 ->return 30
        3 ->return 90
        4 ->return 180
    }
    return  360
}

val CONTIN_LESSON="continLesson" //если нужно продолжить урок то значаение true , влияет на fragment_end_lesson

val COUNTER_EXIST="counterExist" //для установки напоминания


val END_LESSON_STATUS_1=1 //первый урок, прошел или начался
val END_LESSON_STATUS_2=2 //напоминем что будут повтроения
val END_LESSON_STATUS_3=3 //вы повторили






val  ALARM_TYPE_RTC = 100
val ALARM_TYPE_ELAPSED = 101



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




