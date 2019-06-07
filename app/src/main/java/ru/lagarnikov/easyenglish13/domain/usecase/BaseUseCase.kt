package ru.lagarnikov.easyenglish13.domain.usecase

import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.domain.Repositoriy


class BaseUseCase(val repositoriy: Repositoriy) {


    fun getListDataTest(themeName: String): ArrayList<DataSql> {
        return repositoriy.getListDataTest(themeName)
    }

    fun insertDataListTest(newListDataSql: ArrayList<DataSql>, themeName: String) {
        repositoriy.insertDataListTest(newListDataSql,themeName)
    }

    fun replaseDataTest(newDataSql: DataSql, themeName: String) {
        repositoriy.replaseDataTest(newDataSql,themeName)
    }

    fun reLoadAllDataDB(themeName: String) {
        repositoriy.reLoadAllDataDB(themeName)
    }



}