package ru.lagarnikov.easyenglish13.presentation.view.finish_screen

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.FinishDialogBinding
import ru.lagarnikov.easyenglish13.databinding.IntroChildDialogBinding
import ru.lagarnikov.easyenglish13.presentation.IS_THEME_FINISH_CHILD
import ru.lagarnikov.easyenglish13.presentation.SIZE_DIALOG_FRAGMENT
import ru.lagarnikov.easyenglish13.presentation.SIZE_DIALOG_FRAGMENT_FINISH_H
import ru.lagarnikov.easyenglish13.presentation.SIZE_DIALOG_FRAGMENT_FINISH_W
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen

class DialogFinish:DialogFragment(),View.OnClickListener {
    private var isFinishWordTheme=false
    private lateinit var mViewModelStartScreen: ViewModelStartScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundl=this.arguments ?: Bundle()
        mViewModelStartScreen =  ViewModelProviders.of(this.activity!!).get(ViewModelStartScreen::class.java)
        isFinishWordTheme=bundl.getBoolean(IS_THEME_FINISH_CHILD + mViewModelStartScreen.mThemeName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FinishDialogBinding=DataBindingUtil
            .inflate(inflater, R.layout.finish_dialog,container,false)
        binding.textViewLabel.visibility=View.GONE
        binding.mesage=getStringMessage(isFinishWordTheme)
        binding.buttonIntro.setOnClickListener(this)

        return binding.root
    }

    private fun getStringMessage(isFinishWordTheme:Boolean):String{
         if (isFinishWordTheme){
            return resources.getString(R.string.ifinish2)
        }else{
             return resources.getString(R.string.ifinish1)
         }
    }

    override fun onStart() {
        super.onStart()

        // safety check
        if (getDialog() == null)
            return;

        val display = activity!!.getWindowManager().getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width = size.x * SIZE_DIALOG_FRAGMENT_FINISH_W
        val height = size.y * SIZE_DIALOG_FRAGMENT_FINISH_H

        getDialog()!!.getWindow().setLayout(width.toInt(), height.toInt());


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonIntro -> dismiss()
        }
    }
}