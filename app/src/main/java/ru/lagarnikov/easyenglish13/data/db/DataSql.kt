package ru.lagarnikov.easyenglish13.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataSql (@PrimaryKey(autoGenerate = true)  var id: Int?,
               @ColumnInfo(name = "en") val en:String,
               @ColumnInfo(name = "ru") val ru:String,
               @ColumnInfo(name = "done") var done:Int,
               @ColumnInfo(name = "doneA") var doneA:Int,
               @ColumnInfo(name = "doneB") var doneB:Int,
               @ColumnInfo(name = "doneF") var doneF:Int,
               @ColumnInfo(name = "donePath") var donePath:Int,
               @ColumnInfo(name = "errorSum") var errorSum:Int,
               @ColumnInfo(name = "errorA") var errorA:Int,
               @ColumnInfo(name = "errorB") var errorB:Int,
               @ColumnInfo(name = "errorF") var errorF:Int,
               @ColumnInfo(name = "state") var state:Int,
               @ColumnInfo(name = "data") var dataStyd:String){
    constructor():this(null,"","",0,0,0,0,0,0,0,
        0,0,0,"")}


