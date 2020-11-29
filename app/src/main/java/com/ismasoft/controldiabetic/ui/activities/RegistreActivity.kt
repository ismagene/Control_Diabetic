package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.databinding.ActivityRegistreBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import java.util.*
import kotlin.math.absoluteValue

class RegistreActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistreBinding

    private var constants: Constants = Constants
    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    @RequiresApi(Build.VERSION_CODES.O)
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

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        /** Funció quan apretem Continuar al registre **/
        binding.buttonContinuar.setOnClickListener {

//            if(validacionsRegistre()){

                scope.launch {
                    val deferred = async {
                        // Es comprova que l'email no estigui ja registrat
                        viewModel?.onButtonContinuarClicked(binding.loginCorreuElectronic.text.toString())
                    }
                    deferred.await()

                    if (viewModel.mailTrobat.value == false) {
                        anarALaSegonaPaginaDeRegistre()
                    }
                    else{
                        errorCorreuElectronicExistent()
                    }
                }

//            }
        }

        binding.loginNaixament.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.loginNaixament.windowToken, 0)

                binding.loginNaixament.setHintTextColor(colorHintDefault)

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    binding.loginNaixament.setText("$dayOfMonth/$monthOfYear/$year")
                }, year, month, day)

                dpd.show()
            }
        }
        binding.loginNaixament.setOnClickListener {

            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.loginNaixament.windowToken, 0)

            binding.loginNaixament.setHintTextColor(colorHintDefault)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.loginNaixament.setText("$dayOfMonth/$monthOfYear/$year")
            }, year, month, day)

            dpd.show()

        }


        binding.loginNom.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginNom.setHintTextColor(colorHintDefault)
        }
        binding.loginCognom.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginNom.setHintTextColor(colorHintDefault)
        }
        binding.loginCognom2.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginCognom2.setHintTextColor(colorHintDefault)
        }
        binding.loginGenere.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginGenere.setHintTextColor(colorHintDefault)
        }
        binding.loginPes.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginPes.setHintTextColor(colorHintDefault)
        }
        binding.loginAltura.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) binding.loginAltura.setHintTextColor(colorHintDefault)
        }
        binding.loginCorreuElectronic.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                binding.loginCorreuElectronic.setHintTextColor(colorHintDefault)
                binding.loginCorreuElectronic.setTextColor(colorTextDefault)
            }
        }
        binding.loginPassword.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                binding.loginPassword.setHintTextColor(colorHintDefault)
                binding.loginPassword.setTextColor(colorTextDefault)
            }
        }

    }

    private fun errorCorreuElectronicExistent() {
        binding.loginCorreuElectronic.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
        Toast.makeText(this, "Aquest correu electrònic ja està registra't", Toast.LENGTH_SHORT).show()
    }

    private fun anarALaSegonaPaginaDeRegistre() {
        intent = Intent(this, Registre2Activity::class.java)
        val b : Bundle = Bundle()
        b.putString("nom",binding.loginNom.text.toString())
        b.putString("cognom",binding.loginCognom.text.toString())
        b.putString("cognom2",binding.loginCognom2.text.toString())
        b.putString("dataNaixement",binding.loginNaixament.text.toString())
        b.putString("genere",binding.loginGenere.text.toString())
        b.putString("pes",binding.loginPes.text.toString())
        b.putString("altura",binding.loginAltura.text.toString())
        b.putString("correuElectronic",binding.loginCorreuElectronic.text.toString())
        b.putString("contrasenya",binding.loginPassword.text.toString())
        intent.putExtras(b)
        startActivity(intent)
    }

    /** Funció que s'executa al apretar continuar en el registre d'usuari
     *   @param user - email de l'usuari que es registre
     *   @param pass **/
    private fun validacionsRegistre(): Boolean {

        if(binding.loginNom.text == null || binding.loginNom.text.toString() == ""){
            binding.loginNom.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginCognom.text == null || binding.loginCognom.text.toString() == ""){
            binding.loginCognom.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginCognom2.text == null || binding.loginCognom2.text.toString() == ""){
            binding.loginCognom2.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El segon cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginNaixament.text == null || binding.loginNaixament.text.toString() == ""){
            binding.loginNaixament.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginGenere.text == null || binding.loginGenere.text.toString() == ""){
            binding.loginGenere.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El genere és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginPes.text == null || binding.loginPes.text.toString() == ""){
            binding.loginPes.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El Pes és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginAltura.text == null || binding.loginAltura.text.toString() == ""){
            binding.loginAltura.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'altura és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginCorreuElectronic.text == null || binding.loginCorreuElectronic.text.toString() == ""){
            binding.loginCorreuElectronic.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El correu electrònic és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }else{
            var mailCorrecte = android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginCorreuElectronic.text.toString()).matches()
            if(!mailCorrecte){
                binding.loginCorreuElectronic.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(this, "El format del correu electrònic no es correcte", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        if(binding.loginPassword.text == null || binding.loginPassword.text.toString() == "" || binding.loginPassword.text.length < 6){
            if(binding.loginPassword.text.length < 6){
                binding.loginPassword.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(this, "La contrasenya ha de ser mínim de 6 caracters", Toast.LENGTH_SHORT).show()
            }else{
                binding.loginPassword.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(this, "La contrasenya és un camp obligatori", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return true
    }

}