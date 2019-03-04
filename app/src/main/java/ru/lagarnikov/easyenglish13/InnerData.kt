package ru.lagarnikov.easyenglish13

import android.content.SharedPreferences

class InnerData private  constructor(val pref:SharedPreferences){


    companion object {

        lateinit var curentInnerData: InnerData
        fun createPref(pref:SharedPreferences){
                curentInnerData =
                        InnerData(pref)
        }

        fun saveText(key:String,text:String){
            val editor = curentInnerData.pref.edit()
            editor.putString(key, text)
            editor.apply()
        }


        fun loadText(key:String):String{
            return curentInnerData.pref.getString(key ," ") ?:" "
        }

        fun saveBoolean(key:String, value:Boolean){
            val editor= curentInnerData.pref.edit()
            editor.putBoolean(key,value)
            editor.apply()
        }

        fun loadBoolean(key:String):Boolean{
            return curentInnerData.pref.getBoolean(key,false)
        }

        fun saveInt(key:String,number:Int){
            val editor = curentInnerData.pref.edit()
            editor.putInt(key, number)
            editor.apply()
        }

        fun loadInt(key:String):Int{
            return curentInnerData.pref.getInt(key,0)
        }

        fun saveFloat(key:String,number:Float){
            val editor = curentInnerData.pref.edit()
            editor.putFloat(key, number)
            editor.apply()
        }

        fun loadFloat(key:String):Float{
            return curentInnerData.pref.getFloat(key,0F)
        }

    }
}