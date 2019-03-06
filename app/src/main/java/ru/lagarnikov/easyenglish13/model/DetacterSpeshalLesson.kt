package ru.lagarnikov.easyenglish13.model

import android.content.res.Resources
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.getAllThemeData
import java.text.SimpleDateFormat
import java.util.*

class DetacterSpeshalLesson(val mModel: MyViewModel, val resources: Resources){
    private val dateAndTime:Calendar= Calendar.getInstance()
    var mControlnaiaTheme=""

    fun getSpehalStateProgram():StateLesson{
        val status=getStateProgramHelper()
        InnerData.saveText(STATUS_PROGRAMM_SPESIAL,status.name)
        return status
    }

    private fun getStateProgramHelper():StateLesson{
        if (InnerData.loadBoolean(LESSON_USALI_ALRM)){
            return StateLesson.usaliLessonError
        }else if (mModel.mPresenter.needRepitLesson()){
            return StateLesson.repitLessonNeed
        }else if(InnerData.loadBoolean(LESSON_REPIT_ALRM + InnerData.loadText(THEME_CURENT))){
            return StateLesson.repitLessonERROR
        }else if(InnerData.loadBoolean(REPIT_LESSON_CREATED)){
            return StateLesson.repitLessonFinish
        }else if(needControlnaia() && !InnerData.loadBoolean(START_CONTROLNAIA)){
            return StateLesson.controlnaiNeed
        }else if(!needControlnaia() && InnerData.loadBoolean(START_CONTROLNAIA)){
            return StateLesson.controlnaiFinish
        }else if (needControlnaia() && InnerData.loadBoolean(START_CONTROLNAIA)){
            return StateLesson.controlnaiNeedElse
        }
        return StateLesson.usaliStatus
    }

    private fun needControlnaia():Boolean{
        val data= getAllThemeData()
        for(data1 in data){
            val zz=resources.getString(data1.themeName).toUpperCase()
            val dataDoneTheme=InnerData.loadText(CHOOSE_THEME_DONE + zz)

            if (difrentDataMorePeriodDays(dataDoneTheme,dataNow(), getPeriodBetwinControln(InnerData.loadInt(DONE_THEME_STATUS + zz)))){
                mControlnaiaTheme=zz
                return true
            }
        }
        return false
    }

    private fun dataNow():String{
        val simpleDateFormat = SimpleDateFormat("dd-MM-yy",Locale.getDefault())
        return simpleDateFormat.format(dateAndTime.time).toString()
    }

    private fun difrentDataMorePeriodDays(dateDone:String, dateNow:String, period:Int, rezultDay:Boolean=true):Boolean{
        val date1 = dateNow
        val date2 = dateDone

        val format = SimpleDateFormat("dd-MM-yy")

        var dateOne: Date? = null
        var dateTwo: Date? = null

        try {
            dateOne = format.parse(date1)
            dateTwo = format.parse(date2)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {// Количество дней между датами в миллисекундах
            val difference = dateOne!!.time - dateTwo!!.time
            // Перевод количества дней между датами из миллисекунд в дни
            val days: Int
            if (rezultDay) {
                days = (difference / (24 * 60 * 60 * 1000)).toInt()
            }else {
                days = (difference / ( 60 * 60 * 1000)).toInt() //результат часы
            }
            if (days>period){
                return true
            }else{
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }
}