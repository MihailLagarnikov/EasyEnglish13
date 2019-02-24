package ru.lagarnikov.easyenglish13.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.Theme.getOneThemeData
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.AdapterRecLessonWords
import ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData.AdapterRecOneTheme
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
        mModel = ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
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
        mModel.mPresenter.createOreReadLesson(mThemeName)


        binding.recLessonWords.setLayoutManager(GridLayoutManager(context, 1))
        mAdapter=AdapterRecLessonWords(mModel.mPresenter.getCurentLessonWords(),mModel,true)
        binding.recLessonWords.adapter = mAdapter
        binding.textViewLessonNumberX.setText(numLesStr)

        binding.cardStartLesson.setOnClickListener(this)
        binding.cardView3.setOnClickListener(this)


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
            binding.cardStartLesson ->{
                mModel.setNextFragmentName( mModel.mPresenter.getFragmentTestType())}
        }
    }
}