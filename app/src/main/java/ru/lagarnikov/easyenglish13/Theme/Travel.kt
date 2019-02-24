package ru.lagarnikov.easyenglish13.Theme

import ru.lagarnikov.easyenglish13.TestPresenter.DataAnswerAB
import java.util.ArrayList
import java.util.HashMap

class Travel {

    fun getListData(): ArrayList<DataAnswerAB>{
        val data= arrayListOf<DataAnswerAB>()
        data.add(DataAnswerAB("авиакомпания","airlines" ))
        data.add(DataAnswerAB("автобус","bus" ))
        data.add(DataAnswerAB("агентство","agency" ))
        data.add(DataAnswerAB("аэропорт","airport" ))
        data.add(DataAnswerAB("багаж","luggage" ))
        data.add(DataAnswerAB("бензин","gasoline" ))
        data.add(DataAnswerAB("билет","ticket" ))
        data.add(DataAnswerAB("валюта","currency" ))
        data.add(DataAnswerAB("гостевой дом","guest house" ))
        data.add(DataAnswerAB("гостиница","hotel" ))
        data.add(DataAnswerAB("гостиничная сеть","hotel chain" ))
        data.add(DataAnswerAB("граница","border" ))
        return data
    }
}