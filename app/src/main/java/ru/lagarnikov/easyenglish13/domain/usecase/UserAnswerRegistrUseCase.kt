package ru.lagarnikov.easyenglish13.domain.usecase

import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.domain.DB_TRUE
import ru.lagarnikov.easyenglish13.domain.STATE__STUDIED_JUST
import ru.lagarnikov.easyenglish13.presentation.FragmentName
import ru.lagarnikov.easyenglish13.presentation.IS_THEME_FINISH_CHILD

class UserAnswerRegistrUseCase {

    internal fun writeAnswerInList(oldList:ArrayList<DataSql>
                                   , positionId:Int
                                   , fragmentName: FragmentName, themeName:String):ArrayList<DataSql>{
        val newDataSql=getNewValue(oldList.get(positionId),fragmentName,themeName)
        oldList.set(positionId,newDataSql)
        return oldList


    }

    private fun getNewValue(dataSql:DataSql, fragmentName: FragmentName, mThemeName:String):DataSql{
        when(fragmentName){
            FragmentName.TestA -> dataSql.doneA= DB_TRUE
            FragmentName.TestB -> dataSql.doneB= DB_TRUE
            FragmentName.TestF -> dataSql.doneF= DB_TRUE
        }
        if (dataSql.doneA == DB_TRUE && dataSql.doneB == DB_TRUE && dataSql.doneF == DB_TRUE){
            dataSql.state= STATE__STUDIED_JUST
        }
        if (InnerData.loadBoolean(IS_THEME_FINISH_CHILD + mThemeName)){
            if (dataSql.doneA == DB_TRUE || dataSql.doneB == DB_TRUE || dataSql.doneF == DB_TRUE){
                dataSql.state= STATE__STUDIED_JUST
            }
        }
        return dataSql
    }
}