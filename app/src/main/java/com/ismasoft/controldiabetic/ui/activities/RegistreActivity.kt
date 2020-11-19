package com.ismasoft.controldiabetic.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.databinding.ActivityLoginBinding
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel

class RegistreActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        binding = ActivityRegistreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider (this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.buttonRegister.setOnClickListener{
            viewModel.onButtonRegistreClicked(binding)
        }


    }

//    private lateinit var dbReference:DatabaseReference

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_registre)

//        buttonRegister = findViewById<TextView>(R.id.registrarse) as Button
//        buttonRegister.setOnClickListener{
//            when{
//                TextUtils.isEmpty(loginNom.text.toString().trim(){ it <= ' ' }) ->{
//                    Toast.makeText(
//                        this@RegistreActivity,
//                        "Siusplau entra el nom",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//
//            }
//        }


}