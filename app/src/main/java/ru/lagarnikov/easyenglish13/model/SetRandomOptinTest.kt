package ru.lagarnikov.easyenglish13.model

import java.util.*

//Для перемешивания в случайном порядке вариантов ответа для тестов А и В
class SetRandomOptinTest () {
    val randomInt= Random().nextInt(4)
    var i1=0
    var i2=0
    var i3=0
    var i4=0
    var t1True=false
    var t2True=false
    var t3True=false
    var t4True=false
    init {
        when (randomInt){
            0 -> {
                i1=0
                i2=1
                i3=2
                i4=3
                t1True=true
            }
            1 -> {
                i1=1
                i2=0
                i3=2
                i4=3
                t2True=true
            }
            2 -> {
                i1=1
                i2=2
                i3=0
                i4=3
                t3True=true
            }
            3 -> {
                i1=1
                i2=2
                i3=3
                i4=0
                t4True=true
            }
        }
    }
}