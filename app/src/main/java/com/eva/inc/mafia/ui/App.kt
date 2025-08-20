package com.eva.inc.mafia.ui

import android.app.Application
import com.eva.inc.mafia.domain.repository.DomainRepository

class App : Application() {
    val domainRepository = DomainRepository(this)

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        private var application: App? = null

        fun get(): App = application!!
    }
}
