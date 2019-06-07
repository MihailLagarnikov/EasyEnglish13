package ru.lagarnikov.easyenglish13.domain.model

//Класс с данными, для тестов типа А и В  и F, где варианту соответствует boolean значение правильный эта вариант или нет
class DataOptionAnswer(val words:String, val answerWords:String, val answer:Boolean=true) {
}