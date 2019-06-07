package ru.lagarnikov.easyenglish13.domain.usecase

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import java.util.concurrent.TimeUnit

class TimerUseCase {
    private var sd=0
    private lateinit var sub:Disposable
    private var isTimeFinish=false

    internal fun startTimer(finishTimer:Int, uiPresenter: UiPresenter.ViewModelTestFragments, startTimeValue:Int){
        isTimeFinish=false
        sd=startTimeValue
        val obser= Observable.interval(1,TimeUnit.SECONDS)

        sub=obser.map { value -> sd++ }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {a ->
                uiPresenter.setCurentProgress(sd)
                if (sd >= finishTimer){
                    sd=0
                    sub.dispose()
                    isTimeFinish=true
                } }


    }

    internal fun isTimeFinish():Boolean{
        return isTimeFinish
    }

    internal fun disposTimer(){
        if (!sub.isDisposed){
            sub.dispose()
            isTimeFinish=false
        }
    }

    internal fun getCurentTime():Int{
        return sd
    }



}