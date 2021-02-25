package com.bravedroid.connecta.presentation.main

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty

class ActivityBindingHelper<T : ViewDataBinding>(private val activity: FragmentActivity) : Lazy<T> {
    private var binding: T? = null

    override fun isInitialized(): Boolean = binding != null

    override val value: T
        get() = binding ?: DataBindingUtil.bind<T>(getContentView())!!.also { t: T ->
            t.lifecycleOwner = this.activity
            binding = t
        }

    private fun getContentView(): View = checkNotNull(
        activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    ) {
        "Call setContentView or Use Activity's secondary constructor passing layout res id."
    }
}

fun <T : ViewDataBinding> FragmentActivity.activityBinding() = ActivityBindingHelper<T>(this)

fun <T : ViewDataBinding> Fragment.dataBinding(): ReadOnlyProperty<Fragment, T> =
    @Suppress("UNCHECKED_CAST")
    ReadOnlyProperty<Fragment, T> { refFragment, property ->
        val hashCode = property.name.hashCode()
        requireView().getTag(hashCode) as? T
            ?:  DataBindingUtil.bind<T>(requireView())!!.also {
                it.lifecycleOwner = refFragment.viewLifecycleOwner
                it.root.setTag(hashCode, it)
            }
    }
