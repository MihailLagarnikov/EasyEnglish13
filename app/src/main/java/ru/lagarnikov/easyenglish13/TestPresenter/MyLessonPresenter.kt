package ru.lagarnikov.easyenglish13.TestPresenter


import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.Room.DataSql
import ru.lagarnikov.easyenglish13.View.*
import java.text.SimpleDateFormat
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
    var mThemeName= InnerData.loadText(THEME_CURENT)
    private var mNumberLesson=0
    private var mTypeCourse=InnerData.loadInt(CURENT_COURSE)
    private var dateAndTime:Calendar= Calendar.getInstance()
    private var mDataNow:String=""
    private var mRepitLesson=false
    private var mControlnaia=false




    fun createOrReadLesson(themeName:String, contolnaia:Boolean =false){
        mControlnaia=contolnaia
        dateAndTime=Calendar.getInstance()
        getDataNow()
        mThemeName=themeName
        mListItem=mModel.getListDataSqlTest()
        mNumberLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)

        if (InnerData.loadBoolean(LESSON_CREATED + mThemeName) && mTypeCourse==InnerData.loadInt(CURENT_COURSE)) {
            readLesson()
        } else {
            createLesson()

        }
    }

    private fun chooseDataWithControlnaia(){
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE__STUDIED_LONG
                || mListItem.get(n).state == STATE__STUDIED_REPIT_1
                || mListItem.get(n).state == STATE__STUDIED_REPIT_2){
                mListItem.get(n).state= STATE_NOT_STUDIED
                mListItem.get(n).doneA= DB_FALSE
                mListItem.get(n).doneB= DB_FALSE
                mListItem.get(n).doneC= DB_TRUE
                mListItem.get(n).doneD= DB_TRUE
                mListItem.get(n).doneE= DB_FALSE
                mModel.replaseDataSql(mListItem.get(n))
            }
        }
    }



    private fun getDataNow(){
        //дату сегодняшнего дня
        val simpleDateFormat = SimpleDateFormat("dd-MM-yy",Locale.getDefault())
        mDataNow=simpleDateFormat.format(dateAndTime.time).toString()
    }

    //Формирует неизученные слова для нового урока
    private fun createLesson(){
        var maxWords=0
        if (mControlnaia){
            chooseDataWithControlnaia()
            maxWords= MAX_WORD_CONTROLNAIA
        }else{
            maxWords=InnerData.loadInt(CURENT_NUMBER_WARDS)
        }

        var numberWords=0
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE_NOT_STUDIED && numberWords < maxWords){
                mListItem.get(n).state= STATE__STUD_NOW
                if (!mControlnaia) {
                    mListItem[n] =createTypeCourseMoments(mListItem.get(n))
                }
                mModel.replaseDataSql(mListItem.get(n))
                numberWords++
            }
        }
        InnerData.saveBoolean(LESSON_CREATED + mThemeName,true)
        val numbLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)
        mNumberLesson=numbLesson+1
        InnerData.saveInt(LESSON_NUMBER+ mThemeName,numbLesson+1)
        val repitWords=createSimpleRepitWords()
        createMaxLessonProgress(repitWords)
    }

    private fun createSimpleRepitWords():Int{
        var numberWords=0
        if (!mControlnaia) {
            val loi=InnerData.loadInt(CURENT_NUMBER_WARDS)/2
            for (n in 0 until  mListItem.size){
                if (mListItem.get(n).state == STATE__STUDIED_LONG && numberWords < loi){
                    mListItem.get(n).state= STATE__STUDIED_REPIT_NOW
                    mListItem.get(n).doneA= DB_FALSE
                    mListItem.get(n).doneB= DB_FALSE
                    mModel.replaseDataSql(createTypeCourseMoments(mListItem.get(n)))
                    numberWords++
                }
            }
        }

        return numberWords*2
    }

    //В зависимости от выбранного курса обучения некоторые типы тестов помечаются как сделанные или не сделенные
    private fun createTypeCourseMoments(data:DataSql):DataSql{
        if (mTypeCourse==0){
            mTypeCourse=InnerData.loadInt(CURENT_COURSE)
        }
            when (mTypeCourse){
                CURENT_COURSE_1 ->{
                    data.doneA= DB_FALSE
                    data.doneB= DB_FALSE
                    data.doneC=DB_FALSE
                    data.doneD=DB_FALSE
                    data.doneE= DB_FALSE
                }
                CURENT_COURSE_2 ->{
                    data.doneA= DB_FALSE
                    data.doneB= DB_FALSE
                    data.doneC=DB_TRUE
                    data.doneD=DB_TRUE
                    data.doneE= DB_FALSE
                }
                CURENT_COURSE_3 ->{
                    data.doneA=DB_TRUE
                    data.doneB= DB_FALSE
                    data.doneC=DB_TRUE
                    data.doneD=DB_TRUE
                    data.doneE= DB_FALSE
                }
            }
        return data
    }

    //Если урок был созданн, и по какой то причине программа закрылась
    private fun readLesson(){
        var numWor=0
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE__STUD_NOW || mListItem.get(n).state == STATE__STUDIED_JUST ){
                mCurentNumberScrinLesson+=mListItem.get(n).done
                numWor++
            }
        }
        if (numWor !=InnerData.loadInt(CURENT_NUMBER_WARDS)){
            //значит пользователь изменил количество слов которые он хочет изучать за урок, надо заново создовать урок...
            mCurentNumberScrinLesson=0
            for (n in 0 until  mListItem.size){

                if (mListItem.get(n).state == STATE__STUD_NOW ){
                    mListItem.get(n).state=STATE_NOT_STUDIED
                    mModel.replaseDataSql(mListItem.get(n))
                }
                if (mListItem.get(n).state == STATE__STUDIED_JUST){
                    mListItem.get(n).state= STATE__STUDIED_LONG
                    mModel.replaseDataSql(mListItem.get(n))
                }

            }
            createLesson()
            return

        }
        mNumberLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)
        mNumberScriinInLesson=InnerData.loadInt(CURENT_MAX_PROGRESS)
    }

    fun getCurentLessonWords():ArrayList<DataSql>{
        val list= arrayListOf<DataSql>()
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE__STUD_NOW  ){
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
        } else {
            //урок закончился!!!
            if (mControlnaia){
                val statusThemeControln=InnerData.loadInt(DONE_THEME_STATUS + mThemeName)+1
                InnerData.saveInt(DONE_THEME_STATUS + mThemeName,statusThemeControln)
                InnerData.saveText(CHOOSE_THEME_DONE,mDataNow)
            }
            mRepitLesson=false
            mNumberScriinInLesson=0
            mCurentNumberScrinLesson=0
            mLessonDone=false
            InnerData.saveBoolean(LESSON_CREATED + mThemeName,false)
            InnerData.saveText(DATA_LAST_LESSON,mDataNow)
            InnerData.saveBoolean(LESSON_REPIT_ALRM + mThemeName,false)
            InnerData.saveBoolean(CONTROLNAIA_ALARM,false)
            return FragmentEndLesson()
        }
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
        if (!mLessonDone) {
            val randomInt=Random().nextInt(5)+1
            if (!getStateTest(randomInt,dataItem)){
                mEnWards=dataItem.en
                mRuWards=dataItem.ru
                /*mIdInDb=dataItem.id*/
                mCurentDataItem=dataItem
                return getTypTest(randomInt,this)
            }
            return getRandomTestType(dataItem)
        }else{
            return getFragmentTestType()
        }

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
        if (cheeckFinalLesson()) {
            val randomInt=Random().nextInt(InnerData.loadInt(CURENT_NUMBER_WARDS))+1
            return interatorRandomLessonWords(0,randomInt)
        } else {
            mLessonDone=true
            lessonDone()
            changeItemStateAfterLesson()
            return DataSql()
        }

    }

    private fun cheeckFinalLesson():Boolean{
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE__STUD_NOW || mListItem.get(n).state == STATE__STUDIED_REPIT_NOW){
                return true
            }
        }
        return false
    }

    //возвращает неизученное слово из списка слов в уроке
    private fun interatorRandomLessonWords(curentInt:Int, randomInt:Int):DataSql{
        var curent=curentInt
        for (n in 0 until  mListItem.size){
            if (mListItem.get(n).state == STATE__STUD_NOW || mListItem.get(n).state == STATE__STUDIED_REPIT_NOW){
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
    private fun createMaxLessonProgress(repitWords:Int=0){
        if (!mControlnaia) {
            var kk= NUMBER_TYPE_TEST_FULL
            when (mTypeCourse){
                CURENT_COURSE_1 -> kk= NUMBER_TYPE_TEST_FULL
                CURENT_COURSE_2 -> kk= NUMBER_TYPE_TEST_2
                CURENT_COURSE_3 -> kk= NUMBER_TYPE_TEST_3
            }
            mNumberScriinInLesson= repitWords+(InnerData.loadInt(CURENT_NUMBER_WARDS)* kk)

        } else {
            if (mListItem.size< MAX_WORD_CONTROLNAIA){
                mNumberScriinInLesson=mListItem.size
            }else{
                mNumberScriinInLesson= MAX_WORD_CONTROLNAIA
            }
        }
        InnerData.saveInt(CURENT_MAX_PROGRESS,mNumberScriinInLesson)

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
                if (mRepitLesson) {
                    mListItem.get(i).state= STATE__STUDIED_REPIT_2
                } else {
                    mListItem.get(i).state= STATE__STUDIED_LONG
                }
                mModel.replaseDataSql(mListItem.get(i))

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
              if (mCurentDataItem.state== STATE__STUD_NOW) {
                  mCurentDataItem.state=STATE__STUDIED_JUST
                  if (!mRepitLesson) {
                      saveProgressThemeProcent()
                      saveProgressThemeWord()
                      saveProgressALLWords()
                  }
              } else if(mCurentDataItem.state== STATE__STUDIED_REPIT_NOW) {
                  mCurentDataItem.state= STATE__STUDIED_REPIT_1
              }
              mCurentDataItem.dataStyd=mDataNow

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

    //Прогресс изучения по теме целиком в процентах
    private fun saveProgressThemeProcent(full:Boolean=false) {
        if (!full) {
            val newOneStudyWordsProcent:Float=1F/(mListItem.size.toFloat()/100F)
            val progressTheme=InnerData.loadFloat(CHOOSE_THEME_PROGRESS_PROCENT + mThemeName)
            var rez:Float=progressTheme + newOneStudyWordsProcent
            if (rez>100){
                rez=100F
            }
            InnerData.saveFloat(CHOOSE_THEME_PROGRESS_PROCENT + mThemeName,rez)
        } else {
            InnerData.saveFloat(CHOOSE_THEME_PROGRESS_PROCENT + mThemeName,100F)
        }

    }

    private fun round(number: Float, scale: Int): Float {
        val pow = Math.pow(10.0, scale.toDouble()).toInt()
        val tmp = number * pow
        return (if (tmp - tmp.toInt() >= 0.5f) tmp + 1 else tmp).toInt().toFloat() / pow
    }


    //Прогресс изучения по теме целиком в словах
    private fun saveProgressThemeWord() {
        val progressTheme=InnerData.loadInt(CHOOSE_THEME_PROGRESS_WORD + mThemeName)
        val rez:Int=progressTheme + 1
        InnerData.saveInt(CHOOSE_THEME_PROGRESS_WORD + mThemeName,rez)
        if (rez==mListItem.size-1){
            saveProgressThemeProcent(true)
            InnerData.saveBoolean(CHOOSE_THEME_DONE + mThemeName + BOOLEAN_FLAG ,true)
            InnerData.saveText(CHOOSE_THEME_DONE + mThemeName,mDataNow)
        }

    }

    //Сколько всего слов изученно
    private fun saveProgressALLWords() {
        val progressAll=InnerData.loadInt(ALL_WORDS_DONE)
        val rez:Int=progressAll + 1
        InnerData.saveInt(ALL_WORDS_DONE,rez)
    }

    private fun difrentDataMoreTtreDays(date:String):Boolean{
        getDataNow()
        val date1 = mDataNow
        val date2 = date

        val format = SimpleDateFormat("dd-MM-yy")

        var dateOne: Date? = null
        var dateTwo: Date? = null

        try {
            dateOne = format.parse(date1)
            dateTwo = format.parse(date2)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // Количество дней между датами в миллисекундах
        val difference = dateOne!!.time - dateTwo!!.time
        // Перевод количества дней между датами из миллисекунд в дни
        val days = (difference / (24 * 60 * 60 * 1000)).toInt() // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        if (days>3){
            return true
        }else{
            return false
        }
    }

     fun needRepitLesson():Boolean{
        mListItem=mModel.getListDataSqlTest()
        var numberWords=0
        for (n in 0 until  mListItem.size){
            if ((mListItem.get(n).state.equals(STATE__STUDIED_LONG)
                        || mListItem.get(n).state.equals(STATE__STUDIED_REPIT_1))){
                if(difrentDataMoreTtreDays(mListItem.get(n).dataStyd)){
                    numberWords++
                }
            }
        }
        if (numberWords>= NUMBER_WORDS_FOR_REPIT_LESSON){
            return true
        }else{
            return  false
        }
    }

    fun createRepiLesson(){
        var numberWords=0
        mListItem=mModel.getListDataSqlTest()

        for (n in 0 until  mListItem.size){
            if ((mListItem.get(n).state == STATE__STUDIED_LONG
                        || mListItem.get(n).state == STATE__STUDIED_REPIT_1)){
                if(difrentDataMoreTtreDays(mListItem.get(n).dataStyd)){
                    mListItem.get(n).state= STATE__STUD_NOW
                    mListItem.get(n).doneA= DB_FALSE
                    mListItem.get(n).doneB= DB_FALSE
                    mListItem.get(n).doneC= DB_TRUE
                    mListItem.get(n).doneD= DB_TRUE
                    mListItem.get(n).doneE= DB_TRUE
                    mModel.replaseDataSql(createTypeCourseMoments(mListItem.get(n)))
                    numberWords++
                }
            }
        }
        InnerData.saveBoolean(LESSON_CREATED + mThemeName,true)
        InnerData.saveBoolean(LESSON_REPIT_ALRM + mThemeName,true)
        val numbLesson=InnerData.loadInt(LESSON_NUMBER + mThemeName)
        mNumberLesson=numbLesson+1
        InnerData.saveInt(LESSON_NUMBER+ mThemeName,numbLesson+1)
        mNumberScriinInLesson=numberWords*2
        mCurentNumberScrinLesson=0
        mRepitLesson=true
        InnerData.saveBoolean(REPIT_LESSON_CREATED,true)
        getDataNow()
    }







}