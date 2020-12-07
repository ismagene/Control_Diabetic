package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.repository.RegistreRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.Constants.REGISTRE_2_CODE
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel
import kotlinx.coroutines.*
import java.util.*


class RegistreActivity : AppCompatActivity(), RegistreRepositoryInterface {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistreBinding

    private var constants: Constants = Constants
    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    /** Funció que es crea al accedir a l'activitat / vista Registre primera pantalla **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.loginNom.hintTextColors
        colorTextDefault = binding.loginNom.textColors

        // Opcións del spiner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genere,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.loginGenereSpiner.adapter = adapter

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /** Funció quan apretem Continuar al registre **/
        binding.buttonContinuar.setOnClickListener {

            if(validacionsRegistre()){
                // Es comprova que l'email no estigui ja registrat
                viewModel.onButtonContinuarClicked(binding.loginCorreuElectronic.text.toString(),this)
            }
            else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
            }
        }

        /* Recuperem els valors de Hint i de Color del text si han estat marcats com error */
        binding.loginNom.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.loginNom.setHintTextColor(colorHintDefault)
            }
        }
        binding.loginCognom.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.loginNom.setHintTextColor(colorHintDefault)
        }
        binding.loginCognom2.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.loginCognom2.setHintTextColor(colorHintDefault)
        }
        binding.loginNaixament.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                obrirCalendariPerSeleccionarData(binding.loginNaixament.text.toString())
            }
        }
        binding.loginNaixament.setOnClickListener {
            obrirCalendariPerSeleccionarData(binding.loginNaixament.text.toString())
        }
        binding.loginGenereSpiner.setOnTouchListener { view, motionEvent ->
            binding.loginGenere.setTextColor(colorTextDefault)
            view.performClick()
            return@setOnTouchListener true
        }
        binding.loginPes.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.loginPes.setHintTextColor(colorHintDefault)
        }
        binding.loginAltura.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.loginAltura.setHintTextColor(colorHintDefault)
        }
        binding.loginCorreuElectronic.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.loginCorreuElectronic.setHintTextColor(colorHintDefault)
                binding.loginCorreuElectronic.setTextColor(colorTextDefault)
            }
        }
        binding.loginPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.loginPassword.setHintTextColor(colorHintDefault)
                binding.loginPassword.setTextColor(colorTextDefault)
            }
        }

    }

    private fun obrirCalendariPerSeleccionarData(data: String){
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.loginNaixament.setHintTextColor(colorHintDefault)

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        if(data != null && !data.equals("")){
            var parts = data.split("/")
            day =  parts[0].toInt()
            month = parts[1].toInt()-1
            year = parts[2].toInt()
        }

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.loginNaixament.setText("${dayOfMonth.toString().padStart(2,'0')}/${(monthOfYear+1).toString().padStart(2,'0')}/$year")
            },
            year,
            month,
            day
        )

        dpd.show()
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)
    }

    private fun errorCorreuElectronicExistent() {
        binding.loginCorreuElectronic.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
        Toast.makeText(this, "Aquest correu electrònic ja està registra't", Toast.LENGTH_SHORT).show()
    }

    private fun anarALaSegonaPaginaDeRegistre() {
        intent = Intent(this, Registre2Activity::class.java)
        val b : Bundle = Bundle()
        b.putString("nom", binding.loginNom.text.toString())
        b.putString("cognom", binding.loginCognom.text.toString())
        b.putString("cognom2", binding.loginCognom2.text.toString())
        b.putString("dataNaixement", binding.loginNaixament.text.toString())
        b.putString("genere", binding.loginGenereSpiner.selectedItem.toString())
        b.putString("pes", binding.loginPes.text.toString())
        b.putString("altura", binding.loginAltura.text.toString())
        b.putString("correuElectronic", binding.loginCorreuElectronic.text.toString())
        b.putString("contrasenya", binding.loginPassword.text.toString())
        intent.putExtras(b)
        startActivityForResult(intent, constants.REGISTRE_2_CODE)
    }

    /** Funció que s'executa al apretar continuar en el registre d'usuari
     *   @param user - email de l'usuari que es registre
     *   @param pass **/
    private fun validacionsRegistre(): Boolean {

        if (binding.loginNom.text == null || binding.loginNom.text.toString() == "") {
            binding.loginNom.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginCognom.text == null || binding.loginCognom.text.toString() == "") {
            binding.loginCognom.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginCognom2.text == null || binding.loginCognom2.text.toString() == "") {
            binding.loginCognom2.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El segon cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginNaixament.text == null || binding.loginNaixament.text.toString() == "") {
            binding.loginNaixament.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement és un camp obligatori", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (binding.loginGenereSpiner.selectedItem == constants.OPCIO_DEFECTE_SPINER) {
            binding.loginGenere.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El genere és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginPes.text == null || binding.loginPes.text.toString() == "") {
            binding.loginPes.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El Pes és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginAltura.text == null || binding.loginAltura.text.toString() == "") {
            binding.loginAltura.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'altura és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginCorreuElectronic.text == null || binding.loginCorreuElectronic.text.toString() == "") {
            binding.loginCorreuElectronic.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El correu electrònic és un camp obligatori", Toast.LENGTH_SHORT)
                .show()
            return false
        } else {
            var mailCorrecte =
                android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginCorreuElectronic.text.toString())
                    .matches()
            if (!mailCorrecte) {
                binding.loginCorreuElectronic.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(
                    this,
                    "El format del correu electrònic no es correcte",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        if (binding.loginPassword.text == null || binding.loginPassword.text.toString() == ""){
            binding.loginPassword.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La contrasenya és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginPassword.text.length < 6){
            binding.loginPassword.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this,"La contrasenya ha de ser mínim de 6 caracters",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun comprobarExisteixEmailOK() {
        errorCorreuElectronicExistent()
    }
    override fun comprobarExisteixEmailNOK() {
        anarALaSegonaPaginaDeRegistre()
    }

    override fun registreOK() {}
    override fun registreNOK() {}
    override fun registreInsertarOK() {}
    override fun registreInsertarNOK() {}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            constants.REGISTRE_2_CODE -> {
                if (resultCode == RESULT_OK) {
                    finish()
                }
            }
        }
    }


}