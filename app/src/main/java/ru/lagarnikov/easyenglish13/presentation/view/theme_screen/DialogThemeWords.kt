package ru.lagarnikov.easyenglish13.presentation.view.theme_screen

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.FragmentOneThemeBinding
import ru.lagarnikov.easyenglish13.data.model.getOneThemeData
import ru.lagarnikov.easyenglish13.presentation.THEME_NAME_CHILD
import ru.lagarnikov.easyenglish13.presentation.view_model.ViewModelStartScreen
import android.graphics.Point
import ru.lagarnikov.easyenglish13.data.model.getDownFotoThemeWord
import ru.lagarnikov.easyenglish13.data.model.getUpFotoThemeWord
import ru.lagarnikov.easyenglish13.presentation.SIZE_DIALOG_FRAGMENT


class DialogThemeWords:DialogFragment(),View.OnClickListener{

    private lateinit var binding: FragmentOneThemeBinding
    private var mThemeName=""
    private lateinit var mViewModelStartScreen: ViewModelStartScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle=this.arguments ?: Bundle()
        mThemeName=bundle.getString(THEME_NAME_CHILD,"")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_one_theme,
            container, false)
        mViewModelStartScreen =  ViewModelProviders.of(this!!.activity!!).get(ViewModelStartScreen::class.java)
        binding.recThemeWord.setLayoutManager(GridLayoutManager(context, 1))
        binding.recThemeWord.adapter = AdapterRecOneTheme(
            getOneThemeData(
                mThemeName.toUpperCase(),
                resources
            ), mViewModelStartScreen.mViewModelTestFragment
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewThemeName.setText(mThemeName.toUpperCase())
        binding.imageView16.setOnClickListener(this)
        binding.imageView10.setOnClickListener(this)
        binding.imageView9.setOnClickListener(this)
        binding.textView23.setOnClickListener(this)

        binding.imageView16.setImageResource(
            getUpFotoThemeWord(
                mThemeName.toUpperCase(),
                resources
            )
        )
        binding.imageView10.setImageResource(
            getDownFotoThemeWord(
                mThemeName.toUpperCase(),
                resources
            )
        )

    }

    override fun onClick(v: View?) {
        when(v){
            binding.imageView16
                    ,binding.imageView10
                ,binding.imageView9
                ,binding.textView23 -> dismiss()
        }
    }

    override fun dismiss() {
        ViewModelProviders.of(this.activity!!).get(ViewModelStartScreen::class.java).setIsBackgroundWork(false)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        ViewModelProviders.of(this.activity!!).get(ViewModelStartScreen::class.java).setIsBackgroundWork(false)

    }

    override fun onStart() {
        super.onStart()

        // safety check
        if (getDialog() == null)
            return;

        val display = activity!!.getWindowManager().getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width = size.x * SIZE_DIALOG_FRAGMENT
        val height = size.y * SIZE_DIALOG_FRAGMENT

        getDialog()!!.getWindow().setLayout(width.toInt(), height.toInt());

    }


}