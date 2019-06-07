package ru.lagarnikov.easyenglish13.data.repositoriy

import android.content.Context
import android.content.res.Resources
import ru.lagarnikov.easyenglish13.data.db.AppDatabase
import ru.lagarnikov.easyenglish13.domain.Repositoriy
import ru.lagarnikov.easyenglish13.data.db.DataSql
import ru.lagarnikov.easyenglish13.data.model.getOneThemeData
import ru.lagarnikov.easyenglish13.domain.DB_FALSE
import ru.lagarnikov.easyenglish13.domain.STATE_NOT_STUDIED

class TestDataRepositoriy private constructor():Repositoriy {
    private lateinit var db: AppDatabase
    private lateinit var context: Context
    private var mCurentThemeName=""
    private lateinit var mResousce:Resources

    fun setContext(context: Context){
        this.context=context
    }

    private object Holder {
        val INSTANCE = TestDataRepositoriy() }

    companion object {
        val instanse:TestDataRepositoriy by lazy {
            Holder.INSTANCE }
    }

    override fun getListDataTest(themeName: String): ArrayList<DataSql> {
        checkDbExist(themeName)
        return db.sqlDao().getAllData() as ArrayList<DataSql>
    }

    override fun insertDataListTest(newListDataSql: ArrayList<DataSql>, themeName: String) {
        checkDbExist(themeName)
        db.sqlDao().insertDataSql(newListDataSql)
    }

    override fun replaseDataTest(newDataSql: DataSql, themeName: String) {
        checkDbExist(themeName)
        db.sqlDao().replaseData(newDataSql)
    }

    private fun checkDbExist(themeName: String){
        if (!mCurentThemeName.equals(themeName)){
            mCurentThemeName=themeName
            createDB(themeName)
        }
    }

    private fun createDB(tableName:String){
        db= AppDatabase.getInstance(context,tableName)!!
        isDbExist()
    }

    private fun isDbExist() {
        val d = getListDataTest(mCurentThemeName)
        val f=0
        if (d.size < 2) {
            val newListDataSql = arrayListOf<DataSql>()
            val listData =
                getOneThemeData(mCurentThemeName.toUpperCase(), mResousce)
            var n = 1
            for (data in listData) {
                newListDataSql.add(
                    DataSql(
                        n, data.english, data.rus, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE,
                        0, 0, 0, 0, 0,
                        STATE_NOT_STUDIED, ""
                    )
                )
                n++
            }

           insertDataListTest(newListDataSql,mCurentThemeName)
        }
    }

    internal fun setResourse(res:Resources){
        mResousce=res
    }



    override fun reLoadAllDataDB(themeName: String) {
        val newListDataSql = arrayListOf<DataSql>()
        val listData =
            getOneThemeData(themeName.toUpperCase(), mResousce)
        var n = 1
        for (data in listData) {
            newListDataSql.add(
                DataSql(
                    n, data.english, data.rus, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE,
                    0, 0, 0, 0, 0,
                    STATE_NOT_STUDIED, ""
                )
            )
            n++
        }

        insertDataListTest(newListDataSql,themeName)
    }
}