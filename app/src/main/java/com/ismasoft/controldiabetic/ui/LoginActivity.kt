package com.ismasoft.controldiabetic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.viewModel.LoginViewModel
import com.ismasoft.controldiabetic.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider (this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        with(binding){
            registrarse.setOnClickListener(){

                binding.login.setHint("Prova")
//                viewModel.onButtonClicked(username.text.toString(),password.text.toString())
            }
        }
    }

}