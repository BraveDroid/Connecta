package com.bravedroid.connecta.presentation.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bravedroid.connecta.R
import com.bravedroid.connecta.app.ConnectaDemoApplication
import com.bravedroid.connecta.databinding.MainFragmentBinding
import com.bravedroid.connecta.di.DI
import com.bravedroid.connecta.domain.events.EventBus
import com.bravedroid.connecta.presentation.ViewModelFactory
import com.bravedroid.connecta.presentation.events.ShowConnectedStateEvent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var eventBus: EventBus
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DataBindingUtil.inflate<MainFragmentBinding>(
        inflater,
        R.layout.main_fragment,
        container,
        false,
    ).also {
        binding = it
    }.root

    private lateinit var successAnimatorSet: AnimatorSet
    private lateinit var failAnimatorSet: AnimatorSet
    private lateinit var animatorsList: List<AnimatorSet>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.connectedBtn.setOnClickListener {
        }
        binding.notConnectedBtn.setOnClickListener {
        }

        successAnimatorSet = createSuccessAnimatorSet()
        failAnimatorSet = createFailAnimatorSet()
        animatorsList = listOf(successAnimatorSet, failAnimatorSet)
    }

    private fun createFailAnimatorSet(): AnimatorSet = AnimatorSet().apply {
        play(
            ObjectAnimator.ofFloat<View>(
                binding.connectionCheckView,
                View.ALPHA,
                0f, 1f
            ).apply {
                duration = 500
            })

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.connectionCheckView.isVisible = true
                removeAllListeners()
            }
        })
    }

    private fun createSuccessAnimatorSet() = (AnimatorSet()).apply {
        playSequentially(
            ObjectAnimator.ofFloat<View>(
                binding.connectionCheckView,
                View.ALPHA,
                0f, 1f
            ).apply {
                duration = 500
            },
            ObjectAnimator.ofFloat<View>(
                binding.connectionCheckView,
                View.ALPHA,
                1f, 1f
            ).apply {
                duration = 1000
            },
            ObjectAnimator.ofFloat<View>(
                binding.connectionCheckView,
                View.ALPHA,
                1f, 0f
            ).apply {
                duration = 500
            }
        )
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.connectionCheckView.isVisible = false
                removeAllListeners()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectEventBus()

//        lifecycleScope.launch {
//            eventBus.oneShotEventStream
//                .filter { it == ShowConnectedStateEvent }
//                .collect { showConnectedStateUi() }
//        }

        eventBus.oneShotEventStream
            .onEach {
                Timber.tag("EVENTBUS").d(" fragmentMain $it")
            }
            .filter {
                it is ShowConnectedStateEvent
            }
            .onEach { showConnectedStateUi() }
            .launchIn(lifecycleScope)


        injectViewModel()
        viewModel.hasAvailableConnection.observe(viewLifecycleOwner) { connectionStateUiModel ->
            Timber.d("hasAvailableConnection is $connectionStateUiModel")
            if (connectionStateUiModel == ConnectionStateUiModel.DISPLAY_NOT_CONNECTED_STATE) {
                showNotConnectedStateUi()
            }
        }
    }

    private fun showNotConnectedStateUi() {
        animatorsList.forEach {
            it.removeAllListeners()
            it.cancel()
        }
        binding.connectionCheckView.apply {
            setBackgroundColor(Color.RED)
            text = "No Connection "
            isVisible = true
        }
        failAnimatorSet.start()
    }

    private fun showConnectedStateUi() {
        animatorsList.forEach {
            it.removeAllListeners()
            it.cancel()
        }
        binding.connectionCheckView.apply {
            setBackgroundColor(Color.GREEN)
            text = "Back Online "
            isVisible = true
        }
        successAnimatorSet.start()
    }

    private fun injectViewModel() {
        val factory = ViewModelFactory(requireContext())
        viewModel = ViewModelProvider(requireApplication(), factory)[MainViewModel::class.java]
    }

    private fun injectEventBus() {
        eventBus = DI.injectEventBus()
    }

    private fun Fragment.requireApplication() =
        requireContext().applicationContext as ConnectaDemoApplication
}
