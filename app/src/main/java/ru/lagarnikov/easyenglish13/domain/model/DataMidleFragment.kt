package ru.lagarnikov.easyenglish13.domain.model

//Данные которые необходиму фрагментам тестА, тестВ, тестF
class DataMidleFragment(val titleRu:String,
                        val  text1:String="",
                        val text2:String="",
                        val text3:String="",
                        val text4:String="",
                        val  text1Answer:String="",
                        val text2Answer:String="",
                        val text3Answer:String="",
                        val text4Answer:String="",
                        val textTrue:Boolean=false,
                        val text2True:Boolean=false,
                        val text3True:Boolean=false,
                        val text4True:Boolean=false,
                        val titleEn:String){
}