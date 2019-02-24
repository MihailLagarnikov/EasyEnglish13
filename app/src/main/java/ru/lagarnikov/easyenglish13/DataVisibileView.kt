package ru.lagarnikov.easyenglish13

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class DataVisibileView(private  var _answerNotDone:Boolean,private var _answerTrue:Boolean,
                       private var _typeTestA:Boolean,private var _numberAnswer:Int):BaseObservable() {

    var answerNotDone
        @Bindable get() = _answerNotDone
        set(value) {
            _answerNotDone = value
            notifyPropertyChanged(BR.answerNotDone)
        }

    var answerTrue
        @Bindable get() = _answerTrue
        set(value) {
            _answerTrue = value
            notifyPropertyChanged(BR.answerTrue)
        }

    var typeTestA
        @Bindable get() = _typeTestA
        set(value) {
            _typeTestA = value
            notifyPropertyChanged(BR.typeTestA)
        }

    var numberAnswer
        @Bindable get() = _numberAnswer
        set(value) {
            _numberAnswer = value
            notifyPropertyChanged(BR.numberAnswer)
        }

}