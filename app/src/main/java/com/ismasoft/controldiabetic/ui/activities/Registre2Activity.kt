package com.ismasoft.controldiabetic.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.databinding.ActivityRegistre2Binding
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel

class Registre2Activity : AppCompatActivity() {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistre2Binding

    private lateinit var usuari : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre2)

        binding = ActivityRegistre2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider (this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.buttonRegister.setOnClickListener {

            usuari.nom = binding.loginName.toString()
            usuari.primerCognom = binding.loginSurname.toString()

            var retornRegistre1 = viewModel.onButtonRegistreClicked(usuari)
            if(retornRegistre1){
                intent = Intent(this,MenuPrincipalActivity::class.java)
                startActivity(intent)
            }
        }


    }
}