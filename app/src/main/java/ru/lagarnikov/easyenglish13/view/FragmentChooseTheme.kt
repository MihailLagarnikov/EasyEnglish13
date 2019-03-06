package ru.lagarnikov.easyenglish13.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.lagarnikov.easyenglish13.CHOOSE_THEME_PROGRESS_PROCENT
import ru.lagarnikov.easyenglish13.InnerData
import ru.lagarnikov.easyenglish13.MyViewModel
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.getAllThemeData
import ru.lagarnikov.easyenglish13.getOrangeFoto
import ru.lagarnikov.easyenglish13.view.RecyclerViewAdapterAndData.AdapterRecTheme
import ru.lagarnikov.easyenglish13.view.RecyclerViewAdapterAndData.DataAllTheme
import ru.lagarnikov.easyenglish13.databinding.FragmentChooseThemeBinding

class FragmentChooseTheme:Fragment() {
    lateinit var binding:FragmentChooseThemeBinding
    lateinit var mModel: MyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_choose_theme,
            container, false)
        val myView=binding.root
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(false)
        binding.recyclerView.setLayoutManager(GridLayoutManager(context, 1))
        binding.recyclerView.adapter=AdapterRecTheme(getAllThemeDataFull(),mModel,resources)

        binding.imageView8.setOnClickListener(View.OnClickListener {
            mModel.setNextFragmentName(FragmentCreateCourseB())
        })

        mModel.setVisibleAdver(false)

        return myView
    }

    fun getDp():Int{
        return resources.getDisplayMetrics().density.toInt()
    }

    private fun getAllThemeDataFull():ArrayList<DataAllTheme>{
        val data= getAllThemeData()
        val orangeFoto= getOrangeFoto()
        for(i in 0 until data.size){
            val themeName:String=resources.getString(data.get(i).themeName).toUpperCase()
            val progFloat=InnerData.loadFloat(CHOOSE_THEME_PROGRESS_PROCENT + themeName)
            if (progFloat !=0f){
                val textProsOkr=progFloat.toString().substring(0,progFloat.toString().indexOf("."))
                data.get(i).progress=textProsOkr + resources.getString(R.string.theme12)
                data.get(i).imag=orangeFoto.get(i)
            }

        }

        return data
    }
}