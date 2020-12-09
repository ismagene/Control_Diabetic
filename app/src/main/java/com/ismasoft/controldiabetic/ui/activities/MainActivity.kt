package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.databinding.ActivityMainBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.MainViewModel
import kotlinx.coroutines.*
import javax.mail.Flags
import javax.mail.Flags.Flag

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            val deferred = async {
                viewModel.onButtonClicked()
                intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}


