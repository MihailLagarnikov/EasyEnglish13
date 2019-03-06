package ru.lagarnikov.easyenglish13

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import ru.lagarnikov.easyenglish13.view.*
import ru.lagarnikov.easyenglish13.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    lateinit var mAdView: AdView
    lateinit var mModel: MyViewModel
    lateinit var mTextSpeech:TextToSpeech
    private var mIsInitSpeech:Boolean=false
    private var fastSpeedSpeech=true
    private var mLastSpeachWords=""
    lateinit var binding:ActivityMainBinding
    private var mTopFragmentExist=false
    private var mTopFragmentChange=false
    private var mStatusAdver=false
    private lateinit var mFragmentTop:FragmentTop

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        mAdView=binding.adView
        MobileAds.initialize(this, "ca-app-pub-2421174998731562/3002767167");

        createInnerData()
        mModel =  ViewModelProviders.of(this).get(MyViewModel()::class.java)
        createVisibleTopFragmentObserver()
        startBannerAdver()
        createNextFragmentObserver()
        createStartFragmen()
        createVisibleAdvweObserver()
        mTextSpeech=TextToSpeech(this,this);
        createTextSpeachObserver()


    }





    private fun createStartFragmen(){
        when(InnerData.loadInt(STATUS_PROGRAMM)){
            STATUS_1 -> {
                mModel.setNextFragmentName(FragmentEndLesson())
                mModel.createDB(application,InnerData.loadText(THEME_CURENT))
            }
            STATUS_2 ->{
                mModel.setNextFragmentName(FragmentEndLesson())
                InnerData.saveBoolean(CONTIN_LESSON, true)
                mModel.createDB(application, InnerData.loadText(THEME_CURENT))
            }
            STATUS_3 -> mModel.setNextFragmentName(FragmentChooseTheme())
            0 -> {
                if (InnerData.loadInt(CURENT_COURSE)==0){
                    mModel.setNextFragmentName(FragmentCreateCourseA())
                }else if (InnerData.loadInt(CURENT_NUMBER_WARDS)==0){
                    mModel.setNextFragmentName(FragmentCreateCourseB())
                }else{
                    mModel.setNextFragmentName(FragmentChooseTheme())
                }
            }


        }
    }

    private fun createTopFragmen(){
        if (!mTopFragmentExist) {
            mFragmentTop=FragmentTop(mModel.mPresenter)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameTop, mFragmentTop)
            fragmentTransaction.commit()
            mTopFragmentExist=true
        }else{
            mFragmentTop.refreshData()
        }
    }

    private fun createInnerData(){
        InnerData.createPref(this.getPreferences(Context.MODE_PRIVATE))
    }

    private fun doVisibileTopFragment(visib:Boolean){
        if (visib){
            binding.frameTop.visibility=View.VISIBLE
            if (mTopFragmentChange) {
                createTopFragmen()
                mTopFragmentChange=false
            }
        }else{
            binding.frameTop.visibility=View.GONE
            mTopFragmentChange=true
        }
    }

    private fun createNextFragmentObserver(){
        val nameNewFragmentTestObserver = Observer<Fragment> { newName ->
            callFragmentFinish(newName)
        }
        mModel.getNextTestFragmentName().observe(this,nameNewFragmentTestObserver)
    }

    private fun createTextSpeachObserver(){
        val speachObserver= Observer<String> { newText:String ->
            speechGo(newText)}
            mModel.getNextWordSpeach().observe(this,speachObserver)

    }

    private fun createVisibleTopFragmentObserver(){
        val visObs= Observer<Boolean> { newBool ->
            doVisibileTopFragment(newBool)  }
        mModel.getVisibleTopFragment().observe(this,visObs)
    }

    private fun createVisibleAdvweObserver(){
        val visADv= Observer<Boolean> { newBool ->
        if (newBool){
            startBannerAdver()
        }else{
            stopBannerAdver()
        }}
        mModel.getVisibleTopFragment().observe(this,visADv)
    }

    private fun startBannerAdver(){
        if (!mStatusAdver) {
            mAdView.visibility=View.VISIBLE
            mAdView.loadAd(AdRequest.Builder().build())
            mStatusAdver=true
        }

    }

    private fun stopBannerAdver(){
        if (mStatusAdver) {
            mAdView.visibility=View.GONE
            mStatusAdver=false
        }
    }

    private fun callFragmentFinish(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameMidle, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
    }

    override fun onInit(status: Int) {
        if (status === TextToSpeech.SUCCESS) {
            val locale = Locale("en")
            val result = mTextSpeech.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                mIsInitSpeech = false
            } else {
                mIsInitSpeech = true
            }
        } else {
            mIsInitSpeech = false
        }
    }


    fun speechGo(text: String) {
        if (text.equals(mLastSpeachWords)) {
            if (fastSpeedSpeech){
                mTextSpeech.setSpeechRate(SPEED_SPEACH_FAST)
            }else{
                mTextSpeech.setSpeechRate(SPEED_SPEACH_LOW)
            }
            fastSpeedSpeech=fastSpeedSpeech.not()
        } else {
            mTextSpeech.setSpeechRate(SPEED_SPEACH_FAST)
        }

        if (mIsInitSpeech && Build.VERSION.SDK_INT>21){
            mTextSpeech.speak(text, TextToSpeech.QUEUE_ADD,null,"goo")
            mLastSpeachWords=text
        }else if (mIsInitSpeech && Build.VERSION.SDK_INT<=21){
            mTextSpeech.speak(text, TextToSpeech.QUEUE_ADD,null)
            mLastSpeachWords=text
        }else{
            //Какая то ошибка, воспроизведение речи невозможно
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // если это результаты распознавания речи
        // и процесс распознавания прошел успешно
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            // получаем список текстовых строк - результат распознавания
            // строк может быть несколько, так как не всегда удается точно распознать речь
            // более релевантные результаты идут в начале списка
            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // все, в массиве matches мы получили результаты... отобразим в уведомлении самый релевантный
            if (matches.size != 0) mModel.setWordWhatUserSpeak(matches.get(0))
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
