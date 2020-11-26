package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class RegistreActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistreBinding

    private lateinit var usuari : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider (this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.buttonContinuar.setOnClickListener {

            usuari.nom = binding.loginNom.toString()
            usuari.primerCognom = binding.loginCognom.toString()
            usuari.segonCognom = binding.loginCognom2.toString()
//            usuari.dataNaixement = binding.loginNaixament
            usuari.correuElectronic = binding.loginCorreuElectronic.toString()
            usuari.genere = binding.loginGenere.toString()


            var retornRegistre1 = viewModel.onButtonContinuarClicked(usuari)
            if(retornRegistre1){
                intent = Intent(this,MenuPrincipalActivity::class.java)
                startActivity(intent)
            }
        }

        binding.loginNaixament.setOnClickListener(){
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.loginNaixament.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)

            }, year, month, day)

            dpd.show()
        }
    }

}