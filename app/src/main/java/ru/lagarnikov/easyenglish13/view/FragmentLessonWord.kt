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
import ru.lagarnikov.easyenglish13.view.RecyclerViewAdapterAndData.AdapterRecLessonWords
import ru.lagarnikov.easyenglish13.databinding.FragmentWordsLessonBinding

class FragmentLessonWord : Fragment(),View.OnClickListener {
    lateinit var mModel: MyViewModel
    lateinit var mAdapter:AdapterRecLessonWords
    lateinit var binding: FragmentWordsLessonBinding
    private val mThemeName = InnerData.loadText(THEME_CURENT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_words_lesson,
            container, false
        )
        mModel = ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)
        mModel.setVisibleTopFragment(false)

        val myView = binding.root

        val numLes = InnerData.loadInt(LESSON_NUMBER + mThemeName)
        var numLesStr: String = ""
        if (numLes == 0) {
            numLesStr = resources.getString(R.string.theme11 ) + "1"
        } else if (InnerData.loadBoolean(LESSON_CREATED + mThemeName)) {
            //читаем слова у презентера
            numLesStr = resources.getString(R.string.theme11 ) + numLes.toString()
        } else {
            //создаем новый урок
            numLesStr = resources.getString(R.string.theme11 ) + (numLes + 1).toString()
        }
        mModel.mPresenter.createOrReadLesson(mThemeName)


        binding.recLessonWords.setLayoutManager(GridLayoutManager(context, 1))
        val ddatta=mModel.mPresenter.getCurentLessonWords()
        mAdapter=AdapterRecLessonWords(ddatta,mModel,true)
        InnerData.saveInt(NUMBER_WORDS_IN_LESSON,ddatta.size)
        binding.recLessonWords.adapter = mAdapter
        binding.textViewLessonNumberX.setText(numLesStr)

        binding.buttonStartStudy.setOnClickListener(this)
        binding.cardView3.setOnClickListener(this)
        binding.imageView19.setOnClickListener(this)

        mModel.setVisibleAdver(false)


        return myView
    }

    override fun onClick(v: View?) {
        when(v){
            binding.cardView3 ->{
                    mAdapter.openTranslate=binding.textView6.text.toString().equals(resources.getString(R.string.theme9))
                    mAdapter.notifyDataSetChanged()
                if (binding.textView6.text.toString().equals(resources.getString(R.string.theme9))){
                    binding.textView6.text=resources.getString(R.string.theme10)
                }else{
                    binding.textView6.text=resources.getString(R.string.theme9)
                }
            }
            binding.buttonStartStudy -> {
                InnerData.saveBoolean(LESSON_USALI_ALRM + mThemeName,true)
                InnerData.saveInt(STATUS_PROGRAMM, STATUS_2)
                InnerData.saveInt(TIMER_LESSON,0)
                mModel.setNextFragmentName(mModel.mPresenter.getFragmentTestType())
                mModel.startTimer(mThemeName)
            }
            binding.imageView19 ->{
                mModel.setNextFragmentName(FragmentThemeWord())
                InnerData.saveInt(STATUS_PROGRAMM, STATUS_3)
            }
        }
    }
}