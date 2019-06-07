package ru.lagarnikov.easyenglish13.presentation

import io.reactivex.Single
import ru.lagarnikov.easyenglish13.domain.model.DataQuestion

interface DomainInterfase {
    

    interface RxInteratorTest{
        fun startLesson(themeName:String):Single<DataQuestion>
        fun repitLesson(themeName:String):Single<DataQuestion>
        fun cerateNewQuestionData():Single<DataQuestion>
        fun pause()
        fun resume()
        fun destroy()
        fun exitLesson()
        fun answerDone(fragmentName: FragmentName,success:Boolean):Single<Boolean>

    }
}