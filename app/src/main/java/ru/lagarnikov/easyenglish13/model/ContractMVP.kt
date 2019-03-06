package ru.lagarnikov.easyenglish13.model

import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.DataMidleFragment
import ru.lagarnikov.easyenglish13.DataTopFragment
import ru.lagarnikov.easyenglish13.TypeTest

interface ContractMVP {


    interface LessonPresenter{

        fun getFragmentTestType():Fragment
        fun getTestDate(typeTest: TypeTest): DataMidleFragment
        fun getTopFragmentDate(): DataTopFragment
        fun testDone(typeTest: TypeTest, succes: Boolean)
        fun getSuccesAnswer():Boolean
        fun setSuccesAnswer(succes:Boolean)
        fun getCurentProgress():Int
        fun getMaxProgress():Int

    }

}