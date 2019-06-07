package ru.lagarnikov.easyenglish13.presentation.view.intro_screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.lagarnikov.easyenglish13.data.shred_pref.InnerData
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.IntroChildDialogBinding
import ru.lagarnikov.easyenglish13.presentation.IS_SECOND_START_PROGRAMM
import ru.lagarnikov.easyenglish13.presentation.NUMBER_FRAGMENT_INTRO_CHILD
import ru.lagarnikov.easyenglish13.presentation.TAG_INTRO_DIALOG

class DialogIntroChildFRagment:Fragment(), View.OnClickListener {
    private var mNumberFragment=1
    private lateinit var binding: IntroChildDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundl=this.arguments ?: Bundle()
        mNumberFragment=bundl.getInt(NUMBER_FRAGMENT_INTRO_CHILD)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.intro_child_dialog, container, false)
        binding.labelName=getLabelText(mNumberFragment)
        binding.mesage=getMesageText(mNumberFragment)
        binding.imageViewIconChild.setImageDrawable(getDrawble(mNumberFragment))
        binding.visibleBtn=getVisibiliButton(mNumberFragment)
        binding.buttonIntro.setOnClickListener(this)


        return binding.root
    }

    override fun onClick(v: View?) {
        when(v){
          binding.buttonIntro ->{
              InnerData.saveBoolean(IS_SECOND_START_PROGRAMM, true)
              val parentDialog=activity!!.supportFragmentManager.findFragmentByTag(TAG_INTRO_DIALOG)
              if (parentDialog is DialogIntro) {
                  parentDialog.finishDialog()
              }

          }
        }
    }

    private fun getLabelText(numberChildFragment: Int): String {
        return when(numberChildFragment){
            1 -> resources.getString(R.string.introLabel1)
            2 -> resources.getString(R.string.introLabel2)
            else -> resources.getString(R.string.introLabel3)
        }
    }

    private fun getMesageText(numberChildFragment: Int): String {
        return when(numberChildFragment){
            1 -> resources.getString(R.string.intro1)
            2 -> resources.getString(R.string.intro2)
            else -> resources.getString(R.string.intro3)
        }
    }

    private fun getDrawble(numberChildFragment: Int): Drawable {
        return when(numberChildFragment){
            1 -> resources.getDrawable(R.drawable.ic_intro_a)
            2 -> resources.getDrawable(R.drawable.ic_intro_2)
            else -> resources.getDrawable(R.drawable.ic_intro_3)
        }
    }

    private fun getVisibiliButton(numberChildFragment: Int):Boolean{
        return when(numberChildFragment){
            3 -> true
            else -> false
        }
    }

}