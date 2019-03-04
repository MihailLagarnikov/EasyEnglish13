package ru.lagarnikov.easyenglish13.View

import android.app.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentLessonFinishBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.widget.DatePicker
import androidx.core.app.NotificationCompat
import ru.lagarnikov.easyenglish13.MainActivity
import ru.lagarnikov.easyenglish13.notification.NotificationHelper
import java.util.*
import android.widget.TimePicker
import kotlinx.android.synthetic.main.fragment_lesson_finish.*
import ru.lagarnikov.easyenglish13.Theme.getAllThemeData
import java.text.SimpleDateFormat


class FragmentEndLesson:Fragment(),View.OnClickListener {
    lateinit var mModel: MyViewModel
    lateinit var binding:FragmentLessonFinishBinding
    private val mThemeName=InnerData.loadText(THEME_CURENT)
    private val mThemeDone =InnerData.loadBoolean(CHOOSE_THEME_DONE + mThemeName + BOOLEAN_FLAG)
    private val dateAndTime:Calendar= Calendar.getInstance()
    private var mRepitLesson=false
    private var mControlnaia=false
    private var mControlnaiaTheme=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lesson_finish,
            container, false
        )

        //дату сегодняшнего дня
        val simpleDateFormat = SimpleDateFormat("dd MMMM",Locale.getDefault())
        val dataText=simpleDateFormat.format(dateAndTime.time).toString()

        val myView = binding.root
        mModel = ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(false)

        //Надпись на второй текствью сверху
        binding.mStatusProg=InnerData.loadInt(STATUS_PROGRAMM)
        if(InnerData.loadInt(LESSON_NUMBER + mThemeName)==1){
            binding.textView7.text=resources.getString(R.string.lessonEnd12)
        }else{
            binding.textView7.text=resources.getString(R.string.lessonEnd14)
        }


        val numWor=resources.getString(R.string.lessonEnd13) + dataText

        binding.textViewDataTime.text=numWor


        binding.textViewTimeStudy.text=InnerData.loadInt(TIMER_LESSON).toString()
        binding.buttonNextLesson.setOnClickListener(this)
        binding.buttonNextLesson2.setOnClickListener(this)
        binding.imageCLosed.setOnClickListener(this)
        binding.textViewTimeRemember.setOnClickListener(this)
        binding.imageViewTimeRemember.setOnClickListener(this)
        binding.imageView21.setOnClickListener(this)
        binding.imageView22.setOnClickListener(this)
        binding.textViewNumberWordStudy.text=InnerData.loadInt(NUMBER_WORDS_IN_LESSON).toString()
        if (mThemeDone){
            binding.buttonNextLesson.text=resources.getString(R.string.lessonEnd8)
        }
        mModel.stopTimer()
        saveStateProgram()
        needContinLeson()

        setTextRememberTimer()



        mModel.setVisibleAdver(false)
        startStatusLesson()



        return myView

    }

    private fun startStatusLesson(){
        if(mModel.mPresenter.needRepitLesson()){
            mRepitLesson=true
            binding.textView7.text=resources.getString(R.string.lessonEnd15)
            return

        }else if(InnerData.loadBoolean(LESSON_REPIT_ALRM + mThemeName)){
            //Повторный урок оборвался, надо его продолжить!!!!
            mModel.mPresenter.createOrReadLesson(mThemeName)
            mModel.setNextFragmentName(mModel.mPresenter.getFragmentTestType())
            return
        }else if(InnerData.loadBoolean(REPIT_LESSON_CREATED)){
            binding.textView7.text=resources.getString(R.string.lessonEnd16)
            InnerData.saveBoolean(REPIT_LESSON_CREATED,false)
            return
        }

        if (InnerData.loadInt(NUMBER_WORDS_IN_LESSON)==0) {
            binding.textViewNumberWordStudy.text=InnerData.loadInt(CURENT_NUMBER_WARDS).toString()
        } else {
            binding.textViewNumberWordStudy.text=InnerData.loadInt(NUMBER_WORDS_IN_LESSON).toString()
        }


        //Контрольная работа щас начнетс
        if (needControlnaia() && !InnerData.loadBoolean(START_CONTROLNAIA)){
            binding.textView5.text=resources.getString(R.string.lessonEnd18)
            binding.textView7.text=resources.getString(R.string.lessonEnd19)
            binding.buttonNextLesson.text=resources.getString(R.string.lessonEnd20)
            InnerData.saveBoolean(START_CONTROLNAIA,true)

        }
        //Значит контрольная завершена
        if (!needControlnaia() && InnerData.loadBoolean(START_CONTROLNAIA)){
            InnerData.saveBoolean(START_CONTROLNAIA,false)
            binding.textView5.text=resources.getString(R.string.lessonEnd21)
            binding.textView7.text=resources.getString(R.string.lessonEnd22)
            mModel.createDB(activity!!.application,mThemeName)

        }

        if (needControlnaia() && InnerData.loadBoolean(START_CONTROLNAIA)){
            binding.textView5.text=resources.getString(R.string.lessonEnd21)
            binding.textView7.text=resources.getString(R.string.lessonEnd19)
            binding.buttonNextLesson.text=resources.getString(R.string.lessonEnd20)
        }

    }

    private fun saveStateProgram(){
        InnerData.saveInt(STATUS_PROGRAMM, STATUS_1)
    }

    private fun setTextRememberTimer(){
        if (InnerData.loadBoolean(COUNTER_EXIST)){
            binding.textViewTimeRemember.text=resources.getString(R.string.lessonEnd10)
        }else{
            binding.textViewTimeRemember.text=resources.getString(R.string.lessonEnd5)
        }

    }

    private fun needContinLeson(){
        if (InnerData.loadBoolean(CONTIN_LESSON)){
            binding.buttonNextLesson.text=resources.getString(R.string.lessonEnd9)
            InnerData.saveBoolean(CONTIN_LESSON,false)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.buttonNextLesson,binding.buttonNextLesson2 -> {
                if (mRepitLesson){
                    mModel.mPresenter.createRepiLesson()
                    mModel.setNextFragmentName(mModel.mPresenter.getFragmentTestType())
                }else if(InnerData.loadBoolean(START_CONTROLNAIA)){
                    mModel.createDB(activity!!.application,mControlnaiaTheme)
                    mModel.mPresenter.createOrReadLesson(mControlnaiaTheme,true)
                    mModel.setNextFragmentName(mModel.mPresenter.getFragmentTestType())
                }else if (mThemeDone) {
                    mModel.setNextFragmentName(FragmentChooseTheme())
                } else {
                    //Следующий урок
                    if(difrentDataMorePeriodDays(InnerData.loadText(DATA_LAST_LESSON),
                            dataNow(),
                            12,false)) {
                        mModel.setNextFragmentName(FragmentLessonWord())
                    }else{
                        createDilogAboutExitLesson()
                    }

                }
                }

            binding.imageCLosed ->  {
                if (mThemeDone) {
                    mModel.setNextFragmentName(FragmentChooseTheme())
                } else {
                    mModel.setNextFragmentName(FragmentThemeWord())
                }
            }
            binding.textViewTimeRemember,binding.imageViewTimeRemember,binding.imageView21,binding.imageView22 -> {
                if (InnerData.loadBoolean(COUNTER_EXIST)){
                    alarmStop()
                    InnerData.saveBoolean(COUNTER_EXIST,false)

                }else{
                    callCalendar()
                    InnerData.saveBoolean(COUNTER_EXIST,true)
                }
                setTextRememberTimer()

            }
        }
    }

    private fun createDilogAboutExitLesson(){
        val dialog= AlertDialog.Builder(context)
        dialog.setMessage(resources.getString(R.string.dialog4))
        dialog.setPositiveButton(resources.getString(R.string.dialog2), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, arg1: Int) {
                //следующий урок
                mModel.setNextFragmentName(FragmentLessonWord())
            }
        });
        dialog.setNegativeButton(resources.getString(R.string.dialog3),null)
        dialog.show()
    }

    private fun callCalendar() {
        TimePickerDialog(context,t,dateAndTime.get(Calendar.HOUR_OF_DAY),
            dateAndTime.get(Calendar.MINUTE), true).show()


    }

    // установка обработчика выбора времени
    var t: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)
        alarmStart()
    }




    private fun alarmStart(){
        val simpleDateFormat = SimpleDateFormat("HH")
        val simpleDateFormat2 = SimpleDateFormat("mm")
        val strHH:Int = simpleDateFormat.format(dateAndTime.time).toInt()
        val strMM:Int = simpleDateFormat2.format(dateAndTime.time).toInt()
        val milSec=dateAndTime.timeInMillis


         NotificationHelper.scheduleRepeatingRTCNotification(context, "hh", "mm",strHH,strMM,milSec);
        NotificationHelper.enableBootReceiver(context);


    }

    private fun alarmStop(){
        NotificationHelper.cancelAlarmRTC();
        NotificationHelper.disableBootReceiver(context);

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