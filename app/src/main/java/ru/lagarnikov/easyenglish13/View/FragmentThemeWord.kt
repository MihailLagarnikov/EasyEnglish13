package ru.lagarnikov.easyenglish13.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.Room.DataSql
import ru.lagarnikov.easyenglish13.Theme.Travel
import ru.lagarnikov.easyenglish13.Theme.getAllThemeData
import ru.lagarnikov.easyenglish13.Theme.getOneThemeData
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.AdapterRecOneTheme
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.AdapterRecTheme
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.DecoratorRecyclerView
import ru.lagarnikov.easyenglish13.databinding.FragmentOneThemeBinding

class FragmentThemeWord:Fragment(),View.OnClickListener {
    lateinit var mModel: MyViewModel
    private val mThemeName = InnerData.loadText(THEME_CURENT)
    lateinit var binding:FragmentOneThemeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_one_theme,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(false)

        val myView=binding.root
        binding.recThemeWord.setLayoutManager(GridLayoutManager(context, 1))
        binding.recThemeWord.adapter = AdapterRecOneTheme(getOneThemeData(mThemeName, resources), mModel)
        binding.textViewThemeName.setText(mThemeName)
        binding.buttonStartStudy.setOnClickListener(this)
        binding.buttonStartStudyTop.setOnClickListener(this)


        return myView
    }

    override fun onClick(v: View?) {
        when(v){
            binding.buttonStartStudy -> {
                getDb()
                mModel.setNextFragmentName(FragmentLessonWord())
            }
            binding.buttonStartStudyTop -> mModel.setNextFragmentName(FragmentChooseTheme())
        }
    }

    private fun getDb(){
        mModel.createDB(activity!!.application,mThemeName)
        val cc=mModel.getListDataSqlTest()
        if (cc.size<2) {
            val newListDataSql=arrayListOf<DataSql>()
            val travel= Travel()
            var n=1
            for (data in travel.getListData()){
                newListDataSql.add(
                    DataSql(n,data.answerWords,data.words, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE,
                        0,0,0,0,0,0,0,
                        STATE_NOT_STUDIED
                    )
                )
                n++
            }

            mModel.insertDataSql(newListDataSql)
        }
    }


}