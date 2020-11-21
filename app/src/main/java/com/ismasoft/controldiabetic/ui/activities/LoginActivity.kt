package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
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
            /* Funció Login */
            login.setOnClickListener{
                viewModel?.onButtonLoginClicked(binding.username.text.toString(),binding.password.text.toString())
                /* OJOISMA - perquè no se'm actualitza? */
                if (viewModel?.logged?.value == true) {
                    intent = Intent(this, MenuPrincipalActivity::class.java)
                    startActivity(intent)
                }
            }
            /* Funció de registre */
            registrarse.setOnClickListener{
                intent = Intent(this, RegistreActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun Intent(activityLoginBinding: ActivityLoginBinding, java: Class<*>): Intent? {
        intent = Intent(this, java)
        return intent
    }


}