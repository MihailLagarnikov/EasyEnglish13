package ru.lagarnikov.easyenglish13.domain.model

import ru.lagarnikov.easyenglish13.presentation.FragmentName

class DataQuestion(val dataMidleFragment: DataMidleFragment=DataMidleFragment("","","","",""
,"","","","",false,false,false,false,"")
                   , val fragmentName: FragmentName
                   , val isLessonFinish:Boolean =false) {
}