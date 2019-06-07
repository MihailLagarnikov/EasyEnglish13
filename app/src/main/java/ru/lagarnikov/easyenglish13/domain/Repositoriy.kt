package ru.lagarnikov.easyenglish13.domain

import ru.lagarnikov.easyenglish13.data.db.DataSql

interface Repositoriy {
    abstract fun getListDataTest(themeName: String): ArrayList<DataSql>

    abstract fun insertDataListTest(newListDataSql: ArrayList<DataSql>, themeName: String)

    abstract fun replaseDataTest(newDataSql: DataSql, themeName: String)

    abstract fun reLoadAllDataDB(themeName: String)
}