package ru.lagarnikov.easyenglish13.presentation.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.databinding.ActivityMainBinding
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.presentation.*
import ru.lagarnikov.easyenglish13.presentation.view.fragments_test.FragmentTop
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelTestFragment
import java.util.*


class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener,UiPresenter.NavigationFragment {

    lateinit var mAdView: AdView
    lateinit var mViewModelTest: ViewModelTestFragment
    lateinit var mTextSpeech:TextToSpeech
    private var mIsInitSpeech:Boolean=false
    private var fastSpeedSpeech=true
    private var mLastSpeachWords=""
    lateinit var binding:ActivityMainBinding
    private var mTopFragmentExist=false
    private var mTopFragmentChange=false
    private lateinit var mFragmentTop: FragmentTop
    private lateinit var mViewModelStartScreen: ViewModelStartScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        createInnerData()
        super.onCreate(null)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        loadAds()
        createStartFragmen()
        mTextSpeech=TextToSpeech(this,this)
        mViewModelTest =  ViewModelProviders.of(this).get(ViewModelStartScreen::class.java).mViewModelTestFragment
        mViewModelStartScreen =  ViewModelProviders.of(this).get(ViewModelStartScreen::class.java)
        mViewModelStartScreen.setResource(resources)
        createTextspseechObserver()
        createNextFragmentObserver()
    }

    private fun  loadAds(){
        MobileAds.initialize(this, "ca-app-pub-2421174998731562~1772735539");
        mAdView=binding.adView
        mAdView.loadAd(AdRequest.Builder().build())
    }



    private fun createStartFragmen(){

        if (!InnerData.loadBoolean(IS_SECOND_START_PROGRAMM)){
            setNextFragmentName(FragmentName.IntroScreen, true)
        }else{
            setNextFragmentName(FragmentName.StartFragment, true)
        }


    }


    private fun createTopFragmen(){
        if (!mTopFragmentExist) {
            mFragmentTop= FragmentTop()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameTop, mFragmentTop, TAG_FRAGMENT_TOP)
            fragmentTransaction.commit()
            mTopFragmentExist=true
        }
    }

    private fun createInnerData(){
        InnerData.createPref(this.getPreferences(Context.MODE_PRIVATE))
    }

    private fun createTextspseechObserver(){
        val visObs= Observer<String> { newString ->
            speechGo(newString)  }
        mViewModelTest.getNextWordSpeech().observe(this,visObs)
    }

    private fun createNextFragmentObserver(){
        val newFragment= Observer<FragmentName> { newFragmentName ->
            setNextFragmentName(newFragmentName
                ,mViewModelStartScreen.mNavigationPresentor.getNeedAddToBackStack().value ?: false)  }
        mViewModelStartScreen.mNavigationPresentor.getFragmentName().observe(this,newFragment)
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

    private fun visibileTopFragmentDependensOfAnotherFragment(fragmentName: FragmentName){
        val visib=
                when(fragmentName){
                    FragmentName.StartFragment,FragmentName.IntroScreen -> false
                    FragmentName.TestA,FragmentName.TestB,FragmentName.TestF,FragmentName.FragmentTop -> true

                }
        doVisibileTopFragment(visib)
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


    private fun speechGo(text: String) {
        var fastSpeed:Float
        var lowSpeed:Float
        if (Build.VERSION.SDK_INT<24){
            fastSpeed= SPEED_SPEACH_FAST_LOW_24
            lowSpeed= SPEED_SPEACH_LOW_LOW_24
        }else{
            fastSpeed= SPEED_SPEACH_FAST
            lowSpeed= SPEED_SPEACH_LOW
        }
            if (text.equals(mLastSpeachWords)) {
                if (fastSpeedSpeech){
                    mTextSpeech.setSpeechRate(fastSpeed)
                }else{
                    mTextSpeech.setSpeechRate(lowSpeed)
                }
                fastSpeedSpeech=fastSpeedSpeech.not()
            } else {
                mTextSpeech.setSpeechRate(fastSpeed)
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



    override fun setNextFragmentName(fragmentName: FragmentName, isNeedAddToBackStack: Boolean) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameMidle, getFragment(fragmentName), getFragmentTag(fragmentName))
        if (isNeedAddToBackStack) {
            fragmentTransaction.addToBackStack(getFragmentTag(fragmentName))
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()
        visibileTopFragmentDependensOfAnotherFragment(fragmentName)
    }


}
