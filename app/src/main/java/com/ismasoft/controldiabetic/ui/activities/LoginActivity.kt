package com.ismasoft.controldiabetic.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.databinding.ActivityLoginBinding
import com.ismasoft.controldiabetic.viewModel.LoginViewModel
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        with(binding){
            /* Funció Login */
            login.setOnClickListener {
                /* Deseleccionem els valors dels editText perquè s'amagui el teclat i desabilitar els botons perquè fem una crida amb coroutine al Login*/
                binding.login.isEnabled = false
                binding.registrarse.isEnabled = false

                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.username.windowToken, 0)

                scope.launch {
                    val deferred = async {
                        viewModel?.onButtonLoginClicked(binding.username.text.toString(), binding.password.text.toString())
                    }
                    deferred.await()
                    /* OJOISMA - perquè no se'm actualitza? */
                    if (viewModel?.logged?.value == true) {
                        intent = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                    }
                    binding.login.isEnabled = true
                    binding.registrarse.isEnabled = true
                }
            }
        }

        /* Funció de registre */
       binding.registrarse.setOnClickListener{
            intent = Intent(this, RegistreActivity::class.java)
            startActivity(intent)
        }

    }

    private fun Intent(activityLoginBinding: CoroutineScope, java: Class<*>): Intent? {
        intent = Intent(this, java)
        return intent
    }


}