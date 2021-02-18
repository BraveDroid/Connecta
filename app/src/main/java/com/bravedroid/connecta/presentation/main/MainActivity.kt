package com.bravedroid.connecta.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bravedroid.connecta.R
import com.bravedroid.connecta.app.ConnectaDemoApplication
import com.bravedroid.connecta.presentation.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setFragmentsContainer(savedInstanceState)
//        injectViewModel()
//        viewModel.hasAvailableConnection.observe(this) {
//           Toast.makeText(this, "Has available connection $it ", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun setFragmentsContainer(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    MainFragment.newInstance(),
                )
                .commitNow()
        }
    }

//    private fun injectViewModel() {
//        val factory = ViewModelFactory(this)
//        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]//vm
//}
//
//    private fun injectViewModel2() {
//        val factory = ViewModelFactory(applicationContext as ConnectaDemoApplication)
//        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]//vm
//    }
}

