package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.RegistreRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityRegistre2Binding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class Registre2Activity : AppCompatActivity(), RegistreRepositoryInterface {

    private lateinit var viewModel: RegistreViewModel
    private lateinit var binding: ActivityRegistre2Binding

    private var constants: Constants = Constants
    private lateinit var colorHintDefault : ColorStateList
    private lateinit var colorTextDefault : ColorStateList

    private lateinit var usuari : User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistre2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorHintDefault = binding.loginCentre.hintTextColors
        colorTextDefault = binding.loginCentre.textColors

        // Opcións del spiner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.loginDiabetesType,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.loginTipusDiabetisSpiner.adapter = adapter

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.buttonRegister.setOnClickListener {

            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validacionsRegistre()){
                usuari = inicialitzarUsuari(binding)
                viewModel.onButtonRegistreClicked(usuari,this)
            }

        }

        /* Recuperem els valors de Hint i de Color del text si han estat marcats com error */
        binding.loginCentre.setOnFocusChangeListener { _,hasFocus ->
            if(hasFocus){
                binding.loginCentre.setHintTextColor(colorHintDefault)
            }
        }
        binding.loginNomMetge.setOnFocusChangeListener { _,hasFocus ->
            if(hasFocus){
                binding.loginNomMetge.setHintTextColor(colorHintDefault)
            }
        }
        binding.loginEmailMetge.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.loginEmailMetge.setHintTextColor(colorHintDefault)
                binding.loginEmailMetge.setTextColor(colorTextDefault)
            }
        }
        binding.loginTipusDiabetisSpiner.setOnFocusChangeListener { _, hasFocus ->

                binding.loginEmailMetge.setHintTextColor(colorHintDefault)
                binding.loginEmailMetge.setTextColor(colorTextDefault)
        }
        binding.loginTipusDiabetisSpiner.setOnTouchListener { view, motionEvent ->
            binding.loginTipusDiabetis.setTextColor(colorTextDefault)
            view.performClick()
            return@setOnTouchListener true
        }
        binding.loginDataDiagnosi.setOnFocusChangeListener { _, hasFocus->
            if(hasFocus){
                obrirCalendariPerSeleccionarData()
            }
        }
        binding.loginDataDiagnosi.setOnClickListener(){
            obrirCalendariPerSeleccionarData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inicialitzarUsuari(binding: ActivityRegistre2Binding): User {

        var bundle = intent.extras
        val nom : String? = bundle?.getString("nom")
        val cognom: String? = bundle?.getString("cognom")
        val cognom2: String? = bundle?.getString("cognom2")
        val date: String? = bundle?.getString("dataNaixement")
        val dataNaixament = SimpleDateFormat("dd/MM/yyyy").parse(date)
        val genere: String? = bundle?.getString("genere")
        val pes: String? = bundle?.getString("pes")
        val pesNumeric = pes?.toInt()
        val altura: String? = bundle?.getString("altura")
        val alturaNumeric = altura?.toInt()
        val correuElectronic: String? = bundle?.getString("correuElectronic")
        val contrasenya: String? = bundle?.getString("contrasenya")

        val centre = binding.loginCentre.text.toString()
        val poblacioCentre = binding.loginPoblacioCentre.text.toString()
        val nomMetge = binding.loginNomMetge.text.toString()
        val correuElectronicMetge = binding.loginEmailMetge.text.toString()
        val tipusDiabetis = binding.loginTipusDiabetisSpiner.selectedItem.toString()
        val date2 = binding.loginDataDiagnosi.text.toString()
        val dataDiagnosi  = SimpleDateFormat("dd/MM/yyyy").parse(date2)
        var glucosaMoltBaixa : Int
        if(binding.loginGlucosaMoltBaixa.text != null && binding.loginGlucosaMoltBaixa.text.toString().trim() != ""){
            glucosaMoltBaixa  = binding.loginGlucosaMoltBaixa.text.toString().toInt()
        }else{
            glucosaMoltBaixa = 54
        }
        var glucosaBaixa : Int
        if(binding.loginGlucosaBaixa.text != null && binding.loginGlucosaBaixa.text.toString().trim() != ""){
            glucosaBaixa  = binding.loginGlucosaBaixa.text.toString().toInt()
        }else{
           glucosaBaixa = 80
        }
        var glucosaAlta : Int
        if(binding.loginGlucosaAlta.text != null && binding.loginGlucosaAlta.text.toString().trim() != ""){
            glucosaAlta  = binding.loginGlucosaAlta.text.toString().toInt()
        }else{
            glucosaAlta = 130
        }
        var glucosaMoltAlta : Int
        if(binding.loginGlucosaMoltAlta.text != null && binding.loginGlucosaMoltAlta.text.toString().trim() != ""){
            glucosaMoltAlta  = binding.loginGlucosaMoltAlta.text.toString().toInt()
        }else{
            glucosaMoltAlta = 250
        }
        var glucosaBaixaDA : Int
        if(binding.loginGlucosaBaixaDespresApat.text != null && binding.loginGlucosaBaixaDespresApat.text.toString().trim() != ""){
            glucosaBaixaDA  = binding.loginGlucosaBaixaDespresApat.text.toString().toInt()
        }else{
            glucosaBaixaDA = 120
        }
        var glucosaAltaDA : Int
        if(binding.loginGlucosaAltaDespresApat.text != null && binding.loginGlucosaAltaDespresApat.text.toString().trim() != ""){
            glucosaAltaDA  = binding.loginGlucosaAltaDespresApat.text.toString().toInt()
        }else{
            glucosaAltaDA = 120
        }

        return User(nom, cognom, cognom2, dataNaixament, correuElectronic, contrasenya, genere, pesNumeric, alturaNumeric, centre, poblacioCentre, nomMetge, correuElectronicMetge,
                    tipusDiabetis, dataDiagnosi, glucosaMoltBaixa, glucosaBaixa, glucosaAlta, glucosaMoltAlta, glucosaBaixaDA, glucosaAltaDA
        )

    }

    /** Funció que s'executa al apretar continuar en el registre d'usuari
     *   @param user - email de l'usuari que es registre
     *   @param pass **/
    private fun validacionsRegistre(): Boolean {

        if (binding.loginCentre.text == null || binding.loginCentre.text.toString() == "") {
            binding.loginCentre.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom del centre és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginNomMetge.text == null || binding.loginNomMetge.text.toString() == "") {
            binding.loginNomMetge.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom del metge és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.loginEmailMetge.text == null || binding.loginEmailMetge.text.toString() == "") {
            binding.loginEmailMetge.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El correu electrònic del metge és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }else{
            var mailCorrecte = android.util.Patterns.EMAIL_ADDRESS.matcher(binding.loginEmailMetge.text.toString()).matches()
            if(!mailCorrecte){
                binding.loginEmailMetge.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(
                    this,
                    "El format del correu electrònic del metge no es correcte",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        if (binding.loginTipusDiabetisSpiner.selectedItem == constants.OPCIO_DEFECTE_SPINER) {
            binding.loginTipusDiabetis.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El correu electrònic del metge és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.loginDataDiagnosi.text == null || binding.loginDataDiagnosi.text.toString() == ""){
            binding.loginDataDiagnosi.setHintTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun obrirCalendariPerSeleccionarData(){
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.loginDataDiagnosi.setHintTextColor(colorHintDefault)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.loginDataDiagnosi.setText("$dayOfMonth/${monthOfYear+1}/$year")
            },
            year,
            month,
            day
        )

        dpd.show()
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)
    }

    override fun comprobarExisteixEmailOK() {}
    override fun comprobarExisteixEmailNOK() {}

    override fun registreOK() {
        viewModel.registreUsuariABBDD(usuari,this)
    }

    override fun registreNOK() {
        Toast.makeText(this, "Error en el registre de l'usuari", Toast.LENGTH_SHORT).show()
    }

    override fun registreInsertarOK() {

        alert {
            title = "Registre realitzat"
            message("S'ha enviat un correu de confirmació del registre al correu electrònic introduit.")
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()

    }

    override fun registreInsertarNOK() {
        Toast.makeText(this, "Error en el registre de l'usuari", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}

