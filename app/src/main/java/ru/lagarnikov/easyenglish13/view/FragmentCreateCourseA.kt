package ru.lagarnikov.easyenglish13.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.databinding.FragmentCreateCourseStartBinding

class FragmentCreateCourseA:Fragment(),View.OnClickListener {
    lateinit var binding:FragmentCreateCourseStartBinding
    lateinit var mModel: MyViewModel
    private var mValyeCoyrse=InnerData.loadInt(CURENT_COURSE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_create_course_start,
            container, false)
        mModel =  ViewModelProviders.of(this!!.activity!!).get(MyViewModel::class.java)
        mModel.setVisibleTopFragment(false)
        val myView=binding.root
        binding.mCurentCourse=mValyeCoyrse

        binding.card1Cours.setOnClickListener(this)
        binding.card2Cours.setOnClickListener(this)
        binding.card3Cours.setOnClickListener(this)

        mModel.setVisibleAdver(false)
        setCOlorNumber()

        return myView
    }

    private fun setCOlorNumber(){
        binding.imageView.setImageResource(R.drawable.ic_01)
        binding.imageView27.setImageResource(R.drawable.ic_02)
        binding.imageView28.setImageResource(R.drawable.ic_03)
        when(mValyeCoyrse){
            1 ->binding.imageView.setImageResource(R.drawable.ic_01_or)
            2 ->binding.imageView27.setImageResource(R.drawable.ic_02_or)
            3 ->binding.imageView28.setImageResource(R.drawable.ic_03_or)

        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.card1Cours -> mValyeCoyrse= CURENT_COURSE_1
            binding.card2Cours -> mValyeCoyrse= CURENT_COURSE_2
            binding.card3Cours -> mValyeCoyrse= CURENT_COURSE_3
        }
        InnerData.saveInt(CURENT_COURSE,mValyeCoyrse)
        setCOlorNumber()
        mModel.setNextFragmentName(FragmentCreateCourseB())
    }
}