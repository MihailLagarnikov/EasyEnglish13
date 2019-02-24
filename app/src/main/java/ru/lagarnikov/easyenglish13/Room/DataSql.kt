package ru.lagarnikov.easyenglish13.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lagarnikov.easyenglish13.DB_TRUE
import ru.lagarnikov.easyenglish13.STATE__STUDIED_JUST
import ru.lagarnikov.easyenglish13.TypeTest

@Entity
class DataSql (@PrimaryKey(autoGenerate = true)  var id: Int?,
               @ColumnInfo(name = "en") val en:String,
               @ColumnInfo(name = "ru") val ru:String,
               @ColumnInfo(name = "done") var done:Int,
               @ColumnInfo(name = "doneA") var doneA:Int,
               @ColumnInfo(name = "doneB") var doneB:Int,
               @ColumnInfo(name = "doneC") var doneC:Int,
               @ColumnInfo(name = "doneD") var doneD:Int,
               @ColumnInfo(name = "doneE") var doneE:Int,
               @ColumnInfo(name = "donePath") var donePath:Int,
               @ColumnInfo(name = "errorSum") var errorSum:Int,
               @ColumnInfo(name = "erroAr") var errorA:Int,
               @ColumnInfo(name = "errorB") var errorB:Int,
               @ColumnInfo(name = "errorC") var errorC:Int,
               @ColumnInfo(name = "errorD") var errorD:Int,
               @ColumnInfo(name = "errorE") var errorE:Int,
               @ColumnInfo(name = "state") var state:Int) {
    constructor():this(null,"","",0,0,0,0,0,0,0,
        0,0,0,0,0,0,0)}


