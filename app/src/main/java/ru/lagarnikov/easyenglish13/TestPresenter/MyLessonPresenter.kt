package ru.lagarnikov.easyenglish13.TestPresenter


import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.Room.DataSql
import ru.lagarnikov.easyenglish13.View.*
import java.util.*
import kotlin.collections.ArrayList

class MyLessonPresenter(val mModel: MyViewModel): ContractMVP.LessonPresenter {
    lateinit var mListItem:ArrayList<DataSql>
    var mEnWards:String=""
    var mRuWards:String=""
    var mIdInDb:Int=0
    private var mTypeTest=TypeTest.TestA
    private var mNumberScriinInLesson=0
    private var mCurentNumberScrinLesson=0
    lateinit var mCurentDataItem:DataSql
    private var mSuccesAnswer:Boolean=false
    private var mLessonDone=false
    private var mThemeName= InnerData.loadText(THEME_CURENT)
    private var mNumberLesson=0




    fun createOreReadLesson(themeName:String){
        mThemeName=themeName
        mListItem=mModel.getListDataSqlTest()
        mNumberLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)
        if (InnerData.loadBoolean(LESSON_CREATED + mThemeName)) {
            readLesson()
        } else {
            createLesson()

        }
    }

    //Формирует неизученные слова для нового урока
    private fun createLesson(){
        var numberWords=0
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state.equals(STATE_NOT_STUDIED) && numberWords < InnerData.loadInt(CURENT_NUMBER_WARDS)){
                mListItem.get(n).state= STATE__STUD_NOW
                mModel.replaseDataSql(mListItem.get(n))
                numberWords++
            }
        }
        InnerData.saveBoolean(LESSON_CREATED + mThemeName,true)
        val numbLesson=InnerData.loadInt(LESSON_NUMBER)
        mNumberLesson=numbLesson+1
        InnerData.saveInt(LESSON_NUMBER,numbLesson+1)
        createMaxLessonProgress()
    }

    //Если урок был созданн, и по какой то причине программа закрылась
    private fun readLesson(){
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state.equals(STATE__STUD_NOW) || mListItem.get(n).state.equals(STATE__STUDIED_JUST) ){
                mCurentNumberScrinLesson+=mListItem.get(n).done
            }
        }
        mNumberLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)
        createMaxLessonProgress()
    }

    fun getCurentLessonWords():ArrayList<DataSql>{
        val list= arrayListOf<DataSql>()
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state.equals(STATE__STUD_NOW) || mListItem.get(n).state.equals(STATE__STUDIED_JUST) ){
                list.add(mListItem.get(n))
            }
        }

        return list

    }

    //Генерирует новый фрагмент с неизученным видом теста из текущего урока
    override fun getFragmentTestType(): Fragment {
        if (!mLessonDone) {
            mSuccesAnswer=false
           return getRandomTestType(getRandomLessonWord())
        } /*else {
            //урок закончился!!!
            InnerData.saveBoolean(LESSON_CREATED + mThemeName,false)
            return ContractFragmentCallActivity.FragmentName.ChooseThemeFragment

        }*/
        return TestA(this)
    }



    //предоставляет данные в фрагмент с заданием, в зависимости от типа теста
    override fun getTestDate(typeTest: TypeTest): DataMidleFragment {
        mTypeTest=typeTest
        when (typeTest){
            TypeTest.TestA -> return createRandomLessonADate(createListTrueAndFalsWords(mEnWards,mRuWards,true))
            TypeTest.TestB -> return createRandomLessonADate(createListTrueAndFalsWords(mRuWards,mEnWards,false))
            TypeTest.TestC -> return DataMidleFragment(mRuWards, titleEn = mEnWards)
            TypeTest.TestD -> return DataMidleFragment(mEnWards, titleEn = mRuWards)
            TypeTest.TestE -> return DataMidleFragment(mRuWards, titleEn = mEnWards)
        }
    }

    override fun getTopFragmentDate(): DataTopFragment {
        return DataTopFragment(mThemeName, mNumberLesson.toString(), TIMER_MAX_TEXT, mTypeTest)
    }

    //После того как рандомно выбранно не изученное слово из урока, рандомно выбирает неизученный тип теста
    private fun getRandomTestType(dataItem: DataSql):Fragment{
        val randomInt=Random().nextInt(5)+1
        if (!getStateTest(randomInt,dataItem)){
            mEnWards=dataItem.en
            mRuWards=dataItem.ru
            /*mIdInDb=dataItem.id*/
            mCurentDataItem=dataItem
            return getTypTest(randomInt,this)
        }
        return getRandomTestType(dataItem)

    }

    //Если задание изученно возвращает true, иначе false
    private fun getStateTest(i:Int, dataItem: DataSql):Boolean{
        when(i){
            1 -> return getBoleanWithIntDb(dataItem.doneA)
            2 -> return getBoleanWithIntDb(dataItem.doneB)
            3 -> return getBoleanWithIntDb(dataItem.doneC)
            4 -> return getBoleanWithIntDb(dataItem.doneD)
            else -> return getBoleanWithIntDb(dataItem.doneE)
        }
    }


    //возвращает неизученное слово из списка слов в уроке с помощью метода interatorRandomLessonWords
    private fun getRandomLessonWord():DataSql{
        val randomInt=Random().nextInt(InnerData.loadInt(CURENT_NUMBER_WARDS))+1
        return interatorRandomLessonWords(0,randomInt)
    }

    //возвращает неизученное слово из списка слов в уроке
    private fun interatorRandomLessonWords(curentInt:Int, randomInt:Int):DataSql{
        var curent=curentInt
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state.equals(STATE__STUD_NOW)){
                if (curent==randomInt){
                    return mListItem.get(n)
                }
                curent++
            }
        }
        return interatorRandomLessonWords(curent,randomInt)
    }

    //Создает список вариантов ответа на вопрос тестаов типа A и В, один правельный, два - неправельных
    private fun createListTrueAndFalsWords(trueWord:String,trueWordAnswer:String, langEn:Boolean):ArrayList<DataAnswerAB>{
        val listRez= arrayListOf<DataAnswerAB>(
            DataAnswerAB(trueWord, trueWordAnswer,true),
            getRandomAnswerWords(trueWord, langEn)
        )
            listRez.add(getRandomAnswerWords(trueWord, langEn, listRez.get(1).words))
            listRez.add(getRandomAnswerWords(trueWord, langEn, listRez.get(1).words,listRez.get(2).words))
        return listRez
    }

    //Перемешивает в случайном порядке варианты ответа для тестов А и В
    private fun createRandomLessonADate(listData:ArrayList<DataAnswerAB>): DataMidleFragment {
        val randOpt= SetRandomOptinTest()
        return DataMidleFragment(
            mRuWards,
            listData.get(randOpt.i1).words,
            listData.get(randOpt.i2).words,
            listData.get(randOpt.i3).words,
            listData.get(randOpt.i4).words,
            listData.get(randOpt.i1).answerWords,
            listData.get(randOpt.i2).answerWords,
            listData.get(randOpt.i3).answerWords,
            listData.get(randOpt.i4).answerWords,
            randOpt.t1True, randOpt.t2True, randOpt.t3True, randOpt.t4True,
            mEnWards
        )
    }

    //Рандомно выбирает из спска слов урока три слова, которые будут неправильными вариантами ответа
    private fun getRandomAnswerWords(trueWords:String, langEn:Boolean, secondWards:String ="", thredWords:String=""):DataAnswerAB{
        val randomInt=Random().nextInt(mListItem.size)
        if (langEn) {
            if (mListItem.get(randomInt).en.equals(trueWords) || mListItem.get(randomInt).en.equals(secondWards)
                || mListItem.get(randomInt).en.equals(thredWords)){
                return getRandomAnswerWords(trueWords,langEn,secondWards,thredWords)
            }
            return DataAnswerAB(mListItem.get(randomInt).en,mListItem.get(randomInt).ru,false)
        } else {
            if (mListItem.get(randomInt).ru.equals(trueWords)|| mListItem.get(randomInt).ru.equals(secondWards)
                || mListItem.get(randomInt).en.equals(thredWords)){
                return getRandomAnswerWords(trueWords,langEn,secondWards,thredWords)
            }
            return return DataAnswerAB(mListItem.get(randomInt).ru,mListItem.get(randomInt).en,false)
        }
    }


//Вносит изменения в базу после ответа на тест, как успешного так и нет
    override fun testDone(typeTest: TypeTest, succes: Boolean) {
        mSuccesAnswer=succes
        madeTest(typeTest,succes)
        mModel.replaseDataSql(mCurentDataItem)
        changeDataInListLesson(mCurentDataItem)
        if (succes){
            mCurentNumberScrinLesson++
            lessonDone()
        }
    }

    override fun getSuccesAnswer(): Boolean {
        return mSuccesAnswer
    }

    override fun setSuccesAnswer(succes: Boolean) {
        mSuccesAnswer=succes
    }

    //После ответа изменяем данные в списке урока
    private fun changeDataInListLesson(dataItem: DataSql){
        for (i in 0 until mListItem.size){
            if (mListItem.get(i).id==dataItem.id){
                mListItem.set(i,dataItem)

            }
        }
    }

    //Считает количество тестов всего которое будет на уроке
    private fun createMaxLessonProgress(){
        mNumberScriinInLesson= InnerData.loadInt(CURENT_NUMBER_WARDS)* NUMBER_TYPE_TEST

    }

    //Определяет что все тесты урока завершенны
    private fun lessonDone(){
        if (mNumberScriinInLesson==mCurentNumberScrinLesson){
            //урок пройден переходим на экран того что урок закончился
            mLessonDone=true
            changeItemStateAfterLesson()
        }
    }

    override fun getCurentProgress(): Int {
        return mCurentNumberScrinLesson
    }

    override fun getMaxProgress(): Int {
        return mNumberScriinInLesson
    }

    //После того как урок пройден изменяем статус слов с STATE__STUDIED_JUST на STATE__STUDIED_LONG
    private fun changeItemStateAfterLesson(){
        for (i in 0 until mListItem.size){
            if (mListItem.get(i).state== STATE__STUDIED_JUST){
                mListItem.get(i).state= STATE__STUDIED_LONG
                mModel.replaseDataSql(mCurentDataItem)

            }
        }
    }

    //если какойто тип теста в слове успешно выполнен,то записываем изменение в объект
    private fun testSuccessPassed(typeTest: TypeTest){
          when (typeTest){
              TypeTest.TestA -> mCurentDataItem.doneA=DB_TRUE
              TypeTest.TestB -> mCurentDataItem.doneB=DB_TRUE
              TypeTest.TestC -> mCurentDataItem.doneC=DB_TRUE
              TypeTest.TestD -> mCurentDataItem.doneD=DB_TRUE
              TypeTest.TestE -> mCurentDataItem.doneE=DB_TRUE
          }
        mCurentDataItem.done++
          if (mCurentDataItem.doneA==DB_TRUE  && mCurentDataItem. doneB==DB_TRUE
              && mCurentDataItem.doneC==DB_TRUE && mCurentDataItem.doneD==DB_TRUE && mCurentDataItem.doneE==DB_TRUE) {
              mCurentDataItem.state=STATE__STUDIED_JUST
          }
    }

    //Если в каком то типе теста допущенно ошибка,то записываем изменение в объект
    private fun madeErrorInTest(typeTest: TypeTest){
         when (typeTest){
             TypeTest.TestA -> mCurentDataItem.errorA++
             TypeTest.TestB -> mCurentDataItem.errorB++
             TypeTest.TestC -> mCurentDataItem.errorC++
             TypeTest.TestD -> mCurentDataItem.errorD++
             TypeTest.TestE -> mCurentDataItem.errorE++
         }
        mCurentDataItem.errorSum++
    }

    //после выполнения теста вызывается этот метод, который в зависимости от того успешно ли был пройден тест вызывает
    //нужные методы
    fun madeTest(typeTest: TypeTest, success:Boolean){
        if (success){
            testSuccessPassed(typeTest)
        }else{
            madeErrorInTest(typeTest)
        }
    }
}