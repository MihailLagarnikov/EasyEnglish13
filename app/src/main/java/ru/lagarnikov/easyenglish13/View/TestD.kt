package ru.lagarnikov.easyenglish13.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.TestPresenter.MyLessonPresenter
import ru.lagarnikov.easyenglish13.databinding.FragmentMidleTestCDBinding
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import ru.lagarnikov.easyenglish13.*


@SuppressLint("ValidFragment")
open class TestD @SuppressLint("ValidFragment") constructor
    (open val mPresenter: MyLessonPresenter):Fragment(),View.OnClickListener {
    protected val mTypeTest=getTypeTest()
    private var mAnswerNotDone:Boolean=false
    private var mAnswerTrue:Boolean=false
    protected var mDataVisibile=getDataVisibileVIew()
    lateinit var binding: FragmentMidleTestCDBinding
    lateinit var mModel: MyViewModel
    protected val mCurentDataTest=mPresenter.getTestDate(mTypeTest)
    var mError=0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_midle_test_c_d,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel()::class.java)
        mModel.setVisibleTopFragment(true)

        val myView=binding.root
        binding.mData=mCurentDataTest
        binding.mVisibile= mDataVisibile
        mModel.setVisibleElements(mDataVisibile)
        createTextWatcher()
        binding.textViewHintTestCD.setOnClickListener(this)
        callKeyboard()
        setDataTopFr()


        chitTest()

        mModel.setVisibleAdver(true)

        return myView
    }

    private fun chitTest(){
        mDataVisibile.answerTrue=true
        answerDone(true)
    }


    private fun setDataTopFr(){
        try {
            val dataTop: DataTopFragment = mModel.getDataTopFragment().value!!
            dataTop.typeTest=TypeTest.TestD
            mModel.setDataTopFragment(dataTop)
        } catch (e: Exception) {
            //значит верхний фрагмент еще не созданн, ни чего страшного)
        }
    }
    protected open fun getTypeTest(): TypeTest {
        return TypeTest.TestD
    }

    protected open fun getDataVisibileVIew(): DataVisibileView {
        return DataVisibileView(mAnswerNotDone,mAnswerTrue,false,0)
    }

    override fun onClick(v: View?) {
        if(v==binding.textViewHintTestCD){
            answerDone(false)
        }
    }

    private fun createTextWatcher(){
        binding.editTextTestCD.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("asqs","1")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("asqs","1")
            }

            override fun afterTextChanged(s: Editable?) {
                answerLisner(binding.editTextTestCD.text.toString())
            }
        })
    }

    protected  fun answerLisner(text:String){
        val rez: String
        if (mCurentDataTest.titleEn.length>text.length) {
            rez = mCurentDataTest.titleEn.substring(0,text.length)
        } else if (mCurentDataTest.titleEn.length==text.length) {
            rez=mCurentDataTest.titleEn
        }else{
            rez="11111"
            answerDone(false)
        }
        if (!text.equals(rez)){
            answerDone(false)
            mError++
        }else if (mCurentDataTest.titleEn.length==text.length && mCurentDataTest.titleEn.equals(text)){
            answerDone(true)
        }
    }

    private fun answerDone(succes:Boolean){
        if (!mAnswerNotDone) {
            mAnswerNotDone=true
            mDataVisibile.answerNotDone=true
            mDataVisibile.answerTrue=succes
            mModel.setVisibleElements(mDataVisibile)
            mModel.mPresenter.testDone(TypeTest.TestD,mDataVisibile.answerTrue)
        }
    }

    protected open fun callKeyboard() {


        binding.editTextTestCD.post(Runnable {
            val inputMethodManager = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager!!.toggleSoftInputFromWindow(
                binding.editTextTestCD.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY,
                InputMethodManager.SHOW_IMPLICIT
            )
            binding.editTextTestCD.requestFocus()
        })
    }





}