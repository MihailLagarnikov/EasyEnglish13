package ru.lagarnikov.easyenglish13.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.room.DataSql
import ru.lagarnikov.easyenglish13.view.RecyclerViewAdapterAndData.AdapterRecOneTheme
import ru.lagarnikov.easyenglish13.databinding.FragmentOneThemeBinding

class FragmentThemeWord:Fragment(),View.OnClickListener {
    lateinit var mModel: MyViewModel
    private val mThemeName = InnerData.loadText(THEME_CURENT)
    lateinit var binding:FragmentOneThemeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_one_theme,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)
        mModel.setVisibleTopFragment(false)

        val myView=binding.root
        binding.recThemeWord.setLayoutManager(GridLayoutManager(context, 1))
        binding.recThemeWord.adapter = AdapterRecOneTheme(
            getOneThemeData(
                mThemeName,
                resources
            ), mModel)
        binding.textViewThemeName.setText(mThemeName)
        binding.buttonStartStudy.setOnClickListener(this)
        binding.buttonStartStudyTop.setOnClickListener(this)
        binding.imageView9.setOnClickListener(this)
        setDataProgress()
        binding.imageView16.setImageResource(getUpFotoThemeWord(mThemeName, resources))
        binding.imageView10.setImageResource(getDownFotoThemeWord(mThemeName, resources))



        mModel.setVisibleAdver(false)


        return myView
    }

    private fun setDataProgress(){

        val progrStr=InnerData.loadFloat(CHOOSE_THEME_PROGRESS_PROCENT + mThemeName).toString()
        val textProsOkr=progrStr.toString().substring(0,progrStr.toString().indexOf("."))
        binding.textViewProgressThemeOne.text=textProsOkr+ resources.getString(R.string.theme5)

        val progrWordStr=InnerData.loadInt(CHOOSE_THEME_PROGRESS_WORD  + mThemeName).toString()
        binding.textViewNumberWordThemeOne.text=progrWordStr + resources.getString(R.string.theme6)

        val timeTheme=InnerData.loadInt(TIMER_Theme + mThemeName).toString()
        binding.textViewTimeThemeOne.text=timeTheme + resources.getString(R.string.theme4)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.buttonStartStudy,binding.buttonStartStudyTop -> {
                getDb()
                mModel.setNextFragmentName(FragmentLessonWord())
            }
            binding.imageView9 -> mModel.setNextFragmentName(FragmentChooseTheme())
        }
    }

    private fun getDb(){
        mModel.createDB(mThemeName)
        val cc=mModel.getListDataSqlTest()
        if (cc.size<2) {
            val newListDataSql=arrayListOf<DataSql>()
            val listData= getOneThemeData(mThemeName, resources)
            var n=1
            for (data in listData){
                newListDataSql.add(
                    DataSql(n,data.english,data.rus, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE, DB_FALSE,
                        0,0,0,0,0,0,0,
                        STATE_NOT_STUDIED,""
                    )
                )
                n++
            }

            mModel.insertDataSql(newListDataSql)
        }
    }


}