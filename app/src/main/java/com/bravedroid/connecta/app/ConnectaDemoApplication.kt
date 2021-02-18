package com.bravedroid.connecta.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bravedroid.connecta.BuildConfig
import com.bravedroid.connecta.di.system.SystemModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class ConnectaDemoApplication : Application(), ViewModelStoreOwner {
    private lateinit var context: Context
    private val applicationCoroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    private val vms: ViewModelStore by lazy {
        ViewModelStore()
    }

    val systemModule: SystemModule by lazy {
        SystemModule(context, applicationCoroutineContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        context = this
    }

    override fun getViewModelStore(): ViewModelStore = vms
}
