package ru.lagarnikov.easyenglish13

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.lagarnikov.easyenglish13.room.AppDatabase
import ru.lagarnikov.easyenglish13.room.DataSql
import ru.lagarnikov.easyenglish13.model.MyLessonPresenter
import ru.lagarnikov.easyenglish13.model.MyTimer

class MyViewModel():ViewModel() {


    private lateinit var db: AppDatabase
    val mPresenter=MyLessonPresenter(this)
    private var mMyTimer: MyTimer=MyTimer("")

    private val nextFragmentName=MutableLiveData<Fragment>()
    private val nextWordSpeach=MutableLiveData<String>()
    private val wordWhatUserSpeak=MutableLiveData<String>()
    private val visibleElements=MutableLiveData<DataVisibileView>()
    private val dataTopFragment=MutableLiveData<DataTopFragment>()
    private val visibliTopFragment=MutableLiveData<Boolean>()
    private val visibliAdver=MutableLiveData<Boolean>()

    fun setNextFragmentName(newFragment: Fragment){
        nextFragmentName.value=newFragment

    }

    fun getNextTestFragmentName(): LiveData<Fragment> {
        return nextFragmentName
    }

    fun getNextWordSpeach(): LiveData<String> {
        return nextWordSpeach
    }

    fun setNextWordSpeach(text:String){
        nextWordSpeach.value=text
    }

    fun getWordWhatUserSpeak(): LiveData<String> {
        return wordWhatUserSpeak
    }

    fun setWordWhatUserSpeak(text:String){
        wordWhatUserSpeak.value=text
    }

    fun getVisibleElements(): LiveData<DataVisibileView> {
        return visibleElements
    }

    fun setVisibleElements(data:DataVisibileView){
        visibleElements.value=data
    }

    fun getVisibleTopFragment(): LiveData<Boolean> {
        return visibliTopFragment
    }

    fun setVisibleTopFragment(vis:Boolean){
        visibliTopFragment.value=vis
    }

    fun getDataTopFragment(): LiveData<DataTopFragment> {
        return dataTopFragment
    }

    fun setDataTopFragment(dataTop:DataTopFragment){
        dataTopFragment.value=dataTop
    }

    fun getVisibAdver(): LiveData<Boolean> {
        return visibliAdver
    }

    fun setVisibleAdver(vis:Boolean){
        visibliAdver.value=vis
    }



    fun createDB(application:Application, tableName:String){
        db= AppDatabase.getInstance(application,tableName)!!
    }

    fun getListDataSqlTest():ArrayList<DataSql>{
        val date= arrayListOf<DataSql>()
        date.addAll(db.sqlDao().getAllData())
        return date;
    }

    fun insertDataSql(newListDataSql:ArrayList<DataSql>){
        db.sqlDao().insertDataSql(newListDataSql)
    }

    fun replaseDataSql(newDataSql:DataSql){
        db.sqlDao().replaseData(newDataSql)
    }

    fun startTimer(themeName:String){
        mMyTimer=MyTimer(themeName)
        mMyTimer.timerStart()
    }

    fun stopTimer(){
        try {
            mMyTimer.timerStop()
        } catch (e: Exception) {
            //значит таймера еще не существует, ни чего страшного)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}