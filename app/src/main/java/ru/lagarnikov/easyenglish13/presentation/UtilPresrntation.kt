package ru.lagarnikov.easyenglish13.presentation

import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.presentation.view.fragments_test.FragmentTop
import ru.lagarnikov.easyenglish13.presentation.view.fragments_test.TestA
import ru.lagarnikov.easyenglish13.presentation.view.fragments_test.TestB
import ru.lagarnikov.easyenglish13.presentation.view.fragments_test.TestF
import ru.lagarnikov.easyenglish13.presentation.view.intro_screen.DialogIntro
import ru.lagarnikov.easyenglish13.presentation.view.start_screen.StartScreenFragment

enum class FragmentName{
    StartFragment,
    FragmentTop,
    TestA,
    TestB,
    TestF,
    IntroScreen
}

val NUMBER_TEST_FRAGMENT=3
val MAX_TIMER_LESSON_SECONDS=300
val SIZE_DIALOG_FRAGMENT=0.95
val SIZE_DIALOG_FRAGMENT_FINISH_H=0.95
val SIZE_DIALOG_FRAGMENT_FINISH_W=0.99
val NUMBER_CHILD_IN_INTRO=3

//ForSharedPref
val IS_PAUSE_DID="isPause"
val TIMER_CURENT_SEC="timerCurentSec"
val COUNT_ALL_WORD="countAllWord"
val COUNT_THEME_WORD="countTHemeWord"
val THEME_NAME="themeName"
val CURENT_WORDS_THEME_CHILD="curWordsTheme"
val IS_THEME_FINISH_CHILD="themeFinish"
val SAVE_POSITION_THEME="savePOsitionTheme"
val IS_SECOND_START_PROGRAMM="isFirstStartProgramm"


//BundlArguments StartChild Fragment
val THEME_NAME_CHILD="themeName"
val THEME_IMAGE_RES_CHILD="themeImageResource"
val MAX_WORDS_THEME_CHILD="maxWordsTheme"
val POSITION_THEME_CHILD="positionTheme"

//BundlArguments IntroChild Fragment
val NUMBER_FRAGMENT_INTRO_CHILD="numberFragmentIntroChild"


//Fragment Tag
val TAG_START_FRAGMENT="startFr"
val TAG_FRAGMENT_TOP="topFr"
val TAG_FRAGMENT_TEST_A="testAFr"
val TAG_FRAGMENT_TEST_B="testBFr"
val TAG_FRAGMENT_TEST_F="testFFr"
val TAG_INTRO_DIALOG="introDialog"

//Timer Fragment Top
val TIMER_MAX:Long=3000
val TIMER_MAX_2:Long=3000000
val TIMER_INTERVAL:Long=1000

val MAX_SIZE_FRAGMENT_TOP="maxSizeFragmentTop"
val MIN_SIZE_PRESS="maxSizePress"

//For speech play in MainActivity
val SPEED_SPEACH_FAST:Float=0.7F
val SPEED_SPEACH_LOW:Float=0.13F
val SPEED_SPEACH_FAST_LOW_24:Float=1.2F
val SPEED_SPEACH_LOW_LOW_24:Float=0.8F

internal fun getFragmentTag(fragmentName: FragmentName):String{
    return when (fragmentName){
        FragmentName.StartFragment -> TAG_START_FRAGMENT
        FragmentName.FragmentTop -> TAG_FRAGMENT_TOP
        FragmentName.TestA -> TAG_FRAGMENT_TEST_A
        FragmentName.TestB -> TAG_FRAGMENT_TEST_B
        FragmentName.TestF -> TAG_FRAGMENT_TEST_F
        FragmentName.IntroScreen -> TAG_INTRO_DIALOG
    }
}

internal fun getFragment(fragmentName: FragmentName):Fragment{
    return when (fragmentName){
        FragmentName.StartFragment -> StartScreenFragment()
        FragmentName.FragmentTop -> FragmentTop()
        FragmentName.TestA -> TestA()
        FragmentName.TestB -> TestB()
        FragmentName.TestF -> TestF()
        FragmentName.IntroScreen -> DialogIntro()
    }
}

