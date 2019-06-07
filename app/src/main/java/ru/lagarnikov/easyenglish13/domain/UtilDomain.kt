package ru.lagarnikov.easyenglish13.domain

//For study word stats
val STATE_NOT_STUDIED=0
val STATE__STUDIED_LONG=1
val STATE__STUDIED_JUST=2
val STATE__STUD_NOW=3
val DB_FALSE=0
val DB_TRUE=1

val MAX_WORD = 10

fun getBoleanWithIntDb(i:Int):Boolean{
    if (i== DB_FALSE){
        return false
    }
    return true
}