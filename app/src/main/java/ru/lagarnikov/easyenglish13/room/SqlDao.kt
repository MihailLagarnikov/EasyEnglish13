package ru.lagarnikov.easyenglish13.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface SqlDao {

    @Query("SELECT * FROM datasql")
    fun getAllData(): List<DataSql>

    @Update
    fun replaseData(vararg newDataSql:DataSql)

    @Insert(onConflict = REPLACE)
    fun insertDataSql( listDataSql:List<DataSql>)


}