package ru.lagarnikov.easyenglish13

import android.app.Application
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import ru.lagarnikov.easyenglish13.data.repositoriy.TestDataRepositoriy
import ru.lagarnikov.easyenglish13.domain.Repositoriy
import ru.lagarnikov.easyenglish13.domain.interactor.TestFragmentInteractor

@ReportsCrashes(mailTo = "lagarnikov@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_toast_text)
class MyApplication:Application() {
    private val mRepositoriy=TestDataRepositoriy.instanse


    override fun onCreate() {
        super.onCreate()
        mRepositoriy.setContext(applicationContext)
        /*ACRA.init(this)*/
    }




}