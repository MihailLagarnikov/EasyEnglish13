package ru.lagarnikov.easyenglish13.domain.usecase

import ru.lagarnikov.easyenglish13.domain.model.DataMidleFragment
import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.domain.model.DataNameNextsFragmentAndWord
import ru.lagarnikov.easyenglish13.domain.model.DataOptionAnswer
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import java.util.*

/**
 * создает данные для midleFragment (TestA,TestB или TestF) которые содержат варианты ответов
 */
class MidlFragmentDataCreatorUseCase {
    private var mEnWards:String=""
    private var mRuWards:String=""

    fun getTestDate(fragmentDataAndNameNameNextsFragmentAndWord: DataNameNextsFragmentAndWord, mListItem:ArrayList<DataSql>): DataMidleFragment {

        mEnWards=fragmentDataAndNameNameNextsFragmentAndWord.nextEnWord.en
        mRuWards=fragmentDataAndNameNameNextsFragmentAndWord.nextEnWord.ru
        when (fragmentDataAndNameNameNextsFragmentAndWord.nextFragmentName){
            FragmentName.TestA -> return createRandomLessonADate(createListTrueAndFalsWords(mEnWards.trim()
                ,mRuWards.trim(),true,mListItem))
            FragmentName.TestB -> return createRandomLessonADate(createListTrueAndFalsWords(mRuWards.trim()
                ,mEnWards.trim(),false,mListItem))
            FragmentName.TestF -> return createRandomLessonADate(createListTrueAndFalsWords(mRuWards.trim()
                ,mEnWards.trim(),false,mListItem))
        }
        return getTestDate(fragmentDataAndNameNameNextsFragmentAndWord,mListItem)
    }

    //Перемешивает в случайном порядке варианты ответа для тестов А и В
    private fun createRandomLessonADate(listData: ArrayList<DataOptionAnswer>): DataMidleFragment {
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

    //Создает список вариантов ответа на вопрос тестаов типа A и В, один правельный, два - неправельных
    private fun createListTrueAndFalsWords(trueWord:String,trueWordAnswer:String, langEn:Boolean
    ,mListItem:ArrayList<DataSql>):ArrayList<DataOptionAnswer>{
        val listRez= arrayListOf<DataOptionAnswer>(
            DataOptionAnswer(trueWord, trueWordAnswer, true),
            getRandomAnswerWords(trueWord, langEn,"","",mListItem)
        )
        listRez.add(getRandomAnswerWords(trueWord, langEn, listRez.get(1).words,"",mListItem))
        listRez.add(getRandomAnswerWords(trueWord, langEn, listRez.get(1).words,listRez.get(2).words,mListItem))
        return listRez
    }

    //Рандомно выбирает из спска слов урока три слова, которые будут неправильными вариантами ответа
    private fun getRandomAnswerWords(trueWords:String, langEn:Boolean, secondWards:String ="", thredWords:String=""
    ,mListItem:ArrayList<DataSql>): DataOptionAnswer {
        var randomInt= Random().nextInt(mListItem.size)
        if (langEn) {
            if (mListItem.get(randomInt).en.equals(trueWords) || mListItem.get(randomInt).en.equals(secondWards)
                || mListItem.get(randomInt).en.equals(thredWords)){
                return getRandomAnswerWords(trueWords,langEn,secondWards,thredWords,mListItem)
            }
            return DataOptionAnswer(
                mListItem.get(randomInt).en,
                mListItem.get(randomInt).ru,
                false
            )
        } else {
            if (mListItem.get(randomInt).ru.equals(trueWords)|| mListItem.get(randomInt).ru.equals(secondWards)
                || mListItem.get(randomInt).en.equals(thredWords)){
                return getRandomAnswerWords(trueWords,langEn,secondWards,thredWords,mListItem)
            }
            return DataOptionAnswer(
                mListItem.get(randomInt).ru,
                mListItem.get(randomInt).en,
                false
            )
        }
    }
}