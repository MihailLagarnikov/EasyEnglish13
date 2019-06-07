package ru.lagarnikov.easyenglish13.domain.usecase

import ru.lagarnikov.easyenglish13.domain.model.DataNameNextsFragmentAndWord
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import ru.lagarnikov.easyenglish13.presentation.NUMBER_TEST_FRAGMENT
import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.domain.MAX_WORD
import ru.lagarnikov.easyenglish13.domain.STATE__STUD_NOW
import ru.lagarnikov.easyenglish13.domain.getBoleanWithIntDb
import java.util.*

/**
 * Возвращает рандомно тип теста(TestA, TestB или TestC) и строку с данными нового англиского слова
 */
class RandomTestFragmentAndDataUseCase() {

// Возвращает рандомно тип теста(TestA, TestB или TestC) и строку с данными нового англиского слова
    internal fun getFragmentTestType(mListItem: ArrayList<DataSql>): DataNameNextsFragmentAndWord {
        return getRandomTestType(getRandomLessonWord(mListItem))
    }

    //возвращает неизученное слово из списка слов в уроке с помощью метода interatorRandomLessonWords
    private fun getRandomLessonWord(mListItem: ArrayList<DataSql>): DataSql {
        val randomInt = Random().nextInt(MAX_WORD) + 1
        return interatorRandomLessonWords(0, randomInt, mListItem)
    }

    //После того как рандомно выбранно не изученное слово из урока, рандомно выбирает неизученный тип теста
    private fun getRandomTestType(dataItem: DataSql): DataNameNextsFragmentAndWord {
        val randomInt = Random().nextInt(NUMBER_TEST_FRAGMENT) + 1
        if (!getStateTest(randomInt, dataItem)) {
            return DataNameNextsFragmentAndWord(getTypTest(randomInt), dataItem)
        }
        return getRandomTestType(dataItem)
    }

    private fun getTypTest(i: Int): FragmentName {
        when (i) {
            1 -> return FragmentName.TestA
            2 -> return FragmentName.TestB
            3 -> return FragmentName.TestF
        }
        return FragmentName.TestA
    }


    //возвращает неизученное слово из списка слов в уроке
    private fun interatorRandomLessonWords(curentInt: Int, randomInt: Int, mListItem: ArrayList<DataSql>): DataSql {
        var curent = curentInt
        for (n in 0 until mListItem.size) {
            if (mListItem.get(n).state == STATE__STUD_NOW) {
                if (curent == randomInt) {
                    return mListItem.get(n)
                }
                curent++
            }
        }
        return interatorRandomLessonWords(curent, randomInt, mListItem)
    }


    //Если задание изученно возвращает true, иначе false
    private fun getStateTest(i: Int, dataItem: DataSql): Boolean {
        when (i) {
            1 -> return getBoleanWithIntDb(dataItem.doneA)
            2 -> return getBoleanWithIntDb(dataItem.doneB)
            3 -> return getBoleanWithIntDb(dataItem.doneF)
            else -> return getBoleanWithIntDb(dataItem.doneA)
        }
    }

}