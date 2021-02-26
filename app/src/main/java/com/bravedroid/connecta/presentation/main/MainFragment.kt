package com.bravedroid.connecta.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class MainFragment : Fragment(R.layout.main_fragment) {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var eventBus: EventBus
    private lateinit var viewModel: MainViewModel

    //private lateinit var binding: MainFragmentBinding
    private val binding: MainFragmentBinding by dataBinding()

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View = DataBindingUtil.inflate<MainFragmentBinding>(
//        inflater,
//        R.layout.main_fragment,
//        container,
//        false,
//    ).also {
//        binding = it
//    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.connectedBtn.setOnClickListener {
            binding.connectionCheckView.showConnectedStatusUi()
        }
        binding.notConnectedBtn.setOnClickListener {
            binding.connectionCheckView.showNotConnectedStatusUi()
        }
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
            .onEach { binding.connectionCheckView.showConnectedStatusUi() }
            .launchIn(lifecycleScope)


        injectViewModel()
        viewModel.hasAvailableConnection.observe(viewLifecycleOwner) { connectionStateUiModel ->
            Timber.d("hasAvailableConnection is $connectionStateUiModel")
            if (connectionStateUiModel == ConnectionStateUiModel.DISPLAY_NOT_CONNECTED_STATE) {
                binding.connectionCheckView.showNotConnectedStatusUi()
            }
        }
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
