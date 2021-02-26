package com.bravedroid.connecta.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bravedroid.connecta.R
import com.bravedroid.connecta.databinding.MainActivityBinding

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    //private lateinit var viewModel: MainViewModel
    // private lateinit var binding: MainActivityBinding
    private val binding by activityBinding<MainActivityBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.main_activity)
        //binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        setFragmentsContainer(savedInstanceState)
//        injectViewModel()
//        viewModel.hasAvailableConnection.observe(this) {
//           Toast.makeText(this, "Has available connection $it ", Toast.LENGTH_SHORT).show()
//        }
        binding.btn.setOnClickListener {
            Toast.makeText(this, "binding.btn  $it clicked !", Toast.LENGTH_SHORT).show()
        }
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

