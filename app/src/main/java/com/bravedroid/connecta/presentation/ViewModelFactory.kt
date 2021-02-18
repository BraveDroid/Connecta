package com.bravedroid.connecta.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.connecta.di.DI
import com.bravedroid.connecta.domain.usecases.CheckNetworkStateUsesCases
import com.bravedroid.connecta.presentation.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val scm = DI.injectSystemConnectionManagerFlow(context)
            val checkNetworkStateUsesCases = CheckNetworkStateUsesCases(scm)
            val eventBus = DI.injectEventBus()
            return MainViewModel(checkNetworkStateUsesCases, eventBus) as T
        } else {
            throw RuntimeException("Cannot create an instance of $modelClass")
        }
    }
}
