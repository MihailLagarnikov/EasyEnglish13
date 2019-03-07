package ru.lagarnikov.easyenglish13

import android.app.Application
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes

@ReportsCrashes(mailTo = "lagarnikov@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_toast_text)
class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        ACRA.init(this)
    }
}