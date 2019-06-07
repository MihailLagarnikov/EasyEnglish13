package ru.lagarnikov.easyenglish13.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.presentation.FragmentName

class NavigationPresentor:UiPresenter.NavigationFragment {
    private val mFragmentName= MutableLiveData<FragmentName>()
    private val isNeedAddToBackStack= MutableLiveData<Boolean>()
    override fun setNextFragmentName(fragmentName: FragmentName, isNeedAddToBackStack: Boolean) {

        setNeedAddToBackStack(isNeedAddToBackStack)
        setFragmentName(fragmentName)
    }

    fun getFragmentName(): LiveData<FragmentName> {
        return mFragmentName
    }

    private fun setFragmentName(newFragment:FragmentName){
        mFragmentName.value=newFragment
    }

    fun getNeedAddToBackStack(): LiveData<Boolean> {
        return isNeedAddToBackStack
    }

    private fun setNeedAddToBackStack(newBoolean: Boolean){
        isNeedAddToBackStack.value=newBoolean
    }

}