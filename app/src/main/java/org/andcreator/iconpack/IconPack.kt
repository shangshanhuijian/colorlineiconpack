package org.andcreator.iconpack

import android.app.Application
import android.content.Context
import org.andcreator.iconpack.util.CrashHandler

class IconPack : Application() {


    companion object {

        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        CrashHandler.init(applicationContext)
    }
}