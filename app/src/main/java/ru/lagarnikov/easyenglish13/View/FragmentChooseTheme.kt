package ru.lagarnikov.easyenglish13.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.lagarnikov.easyenglish13.MyViewModel
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.Theme.getAllThemeData
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.AdapterRecTheme
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.DecoratorRecyclerView
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
        binding.recyclerView.addItemDecoration(DecoratorRecyclerView(16*getDp()))
        binding.recyclerView.adapter=AdapterRecTheme(getAllThemeData(),mModel,resources)

        return myView
    }

    fun getDp():Int{
        return resources.getDisplayMetrics().density.toInt()
    }
}