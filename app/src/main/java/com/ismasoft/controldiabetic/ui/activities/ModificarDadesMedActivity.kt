package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.interfaces.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarDadesMedBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class ModificarDadesMedActivity : AppCompatActivity() , PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var binding: ActivityModificarDadesMedBinding

    private var constants: Constants = Constants
    private lateinit var colorTextDefault : ColorStateList

    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModificarDadesMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorTextDefault = binding.textValueNomDelCentre.textColors

        // Recuperem info del usuari
        var objetoIntent : Intent = intent
        var usuari : User = objetoIntent.extras?.get("usuariPerfilMed") as User

        // Opcións del spiner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.loginDiabetesType,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.valueTipusDiabetis.adapter = adapter

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.mostrarDadesMediques(usuari)

        // Recuperar el valor del Spiner del tipus de diabetis
        if(viewModel.tipusDiabetis.value.equals("Tipus 1")) {
            binding.valueTipusDiabetis.setSelection(1)
        }else if(viewModel.genere.value.equals("Tipus 2")) {
            binding.valueTipusDiabetis.setSelection(2)
        }else if(viewModel.genere.value.equals("Gestacional")) {
            binding.valueTipusDiabetis.setSelection(3)
        }else if(viewModel.genere.value.equals("LADA o Tipus 1.5")) {
            binding.valueTipusDiabetis.setSelection(4)
        }else if(viewModel.genere.value.equals("MODY")) {
            binding.valueTipusDiabetis.setSelection(5)
        }else if(viewModel.genere.value.equals("Secundaria")) {
            binding.valueTipusDiabetis.setSelection(6)
        }else if(viewModel.genere.value.equals("Sense diagnòsis")) {
            binding.valueTipusDiabetis.setSelection(7)
        }else{
            binding.valueTipusDiabetis.setSelection(0)
        }

        binding.buttonModificarDadesMed.setOnClickListener(){
            hideKeyboard(this)
            // Per modificar-ho
            if(validarEntrada()){
                var usuariAModificar = inicialitzarUsuari()
                viewModel.modificarDadesMed(usuariAModificar, this)
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
            }
        }

        binding.cancelarGuardarDadesMed.setOnClickListener(){
            hideKeyboard(this)
            finish()
        }

        /* Recuperem els valors de Hint i de Color del text si han estat marcats com error */
        binding.valueNomDelCentre.setOnFocusChangeListener { _, hasFocus ->
            if(!primerOnCreate) {
                if(hasFocus) {
                    binding.textValueNomDelCentre.setTextColor(colorTextDefault)
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
                primerOnCreate=false
            }
        }
        binding.valueNomDelCentre.setOnClickListener() {
            binding.textValueNomDelCentre.setTextColor(colorTextDefault)
        }
        binding.valuepoblacioDelCentre.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValuepoblacioDelCentre.setTextColor(colorTextDefault)
        }
        binding.valuepoblacioDelCentre.setOnClickListener() {
            binding.textValuepoblacioDelCentre.setTextColor(colorTextDefault)
        }
        binding.valueNomDelMetge.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValueNomDelMetge.setTextColor(colorTextDefault)
        }
        binding.valueNomDelMetge.setOnClickListener() {
            binding.textValueNomDelMetge.setTextColor(colorTextDefault)
        }
        binding.valuecorreuElectronicMetge.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValuecorreuElectronicMetge.setTextColor(colorTextDefault)
        }
        binding.valuecorreuElectronicMetge.setOnClickListener() {
            binding.textValuecorreuElectronicMetge.setTextColor(colorTextDefault)
        }
        binding.valueTipusDiabetis.setOnTouchListener { view, motionEvent ->
            binding.textValueTipusDiabetis.setTextColor(colorTextDefault)
            view.performClick()
            return@setOnTouchListener true
        }
        binding.valuedataDiagnosi.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                obrirCalendariPerSeleccionarData(binding.valuedataDiagnosi.text.toString())
            }
        }
        binding.valuedataDiagnosi.setOnClickListener() {
            obrirCalendariPerSeleccionarData(binding.valuedataDiagnosi.text.toString())
        }
        binding.glucosaBaixa.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaBaixa.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaBaixa.setOnClickListener() {
            binding.textGlucosaBaixa.setTextColor(colorTextDefault)
        }
        binding.glucosaAlta.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaAlta.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaAlta.setOnClickListener() {
            binding.textGlucosaAlta.setTextColor(colorTextDefault)
        }
        binding.glucosaMoltBaixa.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaMoltBaixa.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaMoltBaixa.setOnClickListener() {
            binding.textGlucosaMoltBaixa.setTextColor(colorTextDefault)
        }
        binding.glucosaMoltAlta.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaMoltAlta.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaMoltAlta.setOnClickListener() {
            binding.textGlucosaMoltAlta.setTextColor(colorTextDefault)
        }
        binding.glucosaBaixaDA.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaBaixaDA.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaBaixaDA.setOnClickListener() {
            binding.textGlucosaBaixaDA.setTextColor(colorTextDefault)
        }
        binding.glucosaAltaDA.setOnFocusChangeListener(){ _, hasFocus ->
            if(hasFocus){
                binding.textGlucosaAltaDA.setTextColor(colorTextDefault)
            }
        }
        binding.glucosaAltaDA.setOnClickListener() {
            binding.textGlucosaAltaDA.setTextColor(colorTextDefault)
        }

    }

    private fun validarEntrada(): Boolean {
        if (binding.valueNomDelCentre.text == null || binding.valueNomDelCentre.text.toString() == "") {
            binding.textValueNomDelCentre.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom del centre és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valuepoblacioDelCentre.text == null || binding.valuepoblacioDelCentre.text.toString() == "") {
            binding.textValuepoblacioDelCentre.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La població del centre és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueNomDelMetge.text == null || binding.valueNomDelMetge.text.toString() == "") {
            binding.textValueNomDelMetge.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom del metge és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valuecorreuElectronicMetge.text == null || binding.textValuecorreuElectronicMetge.text.toString() == "") {
            binding.textValuecorreuElectronicMetge.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(
                this,
                "El correu electrònic del metge és un camp obligatori",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else{
            var mailCorrecte = android.util.Patterns.EMAIL_ADDRESS.matcher(binding.valuecorreuElectronicMetge.text.toString()).matches()
            if(!mailCorrecte){
                binding.textValuecorreuElectronicMetge.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
                Toast.makeText(
                    this,
                    "El format del correu electrònic del metge no es correcte",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        if (binding.valueTipusDiabetis.selectedItem == constants.OPCIO_DEFECTE_SPINER) {
            binding.textValueTipusDiabetis.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(
                this,
                "El tipus de diabetis és un camp obligatori",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if(binding.valuedataDiagnosi.text == null || binding.valuedataDiagnosi.text.toString() == ""){
            binding.textValuedataDiagnosi.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validem que la data no sigui superior o igual a la del dia
        val dataIntroduida = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("${binding.valuedataDiagnosi.text.toString()} 00:00")
        val dataActual = Date()
        if(dataIntroduida.after(dataActual))
        {
            binding.textValuedataDiagnosi.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de diagnosi no pot ser superior a l'actual'", Toast.LENGTH_SHORT).show()
            return false
        }

        // En la modificació els valors dels rangs de glucosa seràn obligatoris
        if(binding.glucosaBaixa.text == null || binding.glucosaBaixa.text.toString() == "" || binding.glucosaBaixa.text.toString() == "0"){
            binding.textGlucosaBaixa.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Baixa és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosaAlta.text == null || binding.glucosaAlta.text.toString() == "" || binding.glucosaAlta.text.toString() == "0"){
            binding.textGlucosaAlta.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Alta és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosaMoltBaixa.text == null || binding.glucosaMoltBaixa.text.toString() == "" || binding.glucosaMoltBaixa.text.toString() == "0"){
            binding.textGlucosaMoltBaixa.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Molt Baixa és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosaMoltAlta.text == null || binding.glucosaMoltAlta.text.toString() == "" || binding.glucosaMoltAlta.text.toString() == "0"){
            binding.textGlucosaMoltAlta.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Molt Alta és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosaBaixaDA.text == null || binding.glucosaBaixaDA.text.toString() == "" || binding.glucosaBaixaDA.text.toString() == "0"){
            binding.textGlucosaBaixaDA.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Baixa desprès d'un apat és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.glucosaAltaDA.text == null || binding.glucosaAltaDA.text.toString() == "" || binding.glucosaAltaDA.text.toString() == "0"){
            binding.textGlucosaAltaDA.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El valor de la glucosa Alta desprès d'un apat és obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

    private fun inicialitzarUsuari(): User {

        val centre : String? = binding.valueNomDelCentre.text.toString()
        val poblacioCentre: String? = binding.valuepoblacioDelCentre.text.toString()
        val nomDelMetge: String? = binding.valueNomDelMetge.text.toString()
        val correuElectronicMetge: String? = binding.valuecorreuElectronicMetge.text.toString()
        val tipuDiabetis: String? = binding.valueTipusDiabetis.selectedItem.toString()
        val date: String? = binding.valuedataDiagnosi.text.toString()
        val dataDiagnosi = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date 00:00")
        val glucosaBaixaString: String? = binding.glucosaBaixa.text.toString()
        var parts = glucosaBaixaString!!.split(" ")
        val glucosaBaixa = parts[0].toInt()
        val glucosaAltaString: String? = binding.glucosaAlta.text.toString()
        parts = glucosaAltaString!!.split(" ")
        val glucosaAlta = parts[0].toInt()
        val glucosaMoltBaixaString: String? = binding.glucosaMoltBaixa.text.toString()
        parts = glucosaMoltBaixaString!!.split(" ")
        val glucosaMoltBaixa = parts[0].toInt()
        val glucosaMoltAltaString: String? = binding.glucosaMoltAlta.text.toString()
        parts = glucosaMoltAltaString!!.split(" ")
        val glucosaMoltAlta = parts[0].toInt()
        val glucosaBaixaDAString: String? = binding.glucosaBaixaDA.text.toString()
        parts = glucosaBaixaDAString!!.split(" ")
        val glucosaBaixaDA = parts[0].toInt()
        val glucosaAltaDAString: String? = binding.glucosaAltaDA.text.toString()
        parts = glucosaAltaDAString!!.split(" ")
        val glucosaAltaDA = parts[0].toInt()

        return User(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            centre,
            poblacioCentre,
            nomDelMetge,
            correuElectronicMetge,
            tipuDiabetis,
            dataDiagnosi,
            glucosaMoltBaixa,
            glucosaBaixa,
            glucosaAlta,
            glucosaMoltAlta,
            glucosaBaixaDA,
            glucosaAltaDA
        )
    }

    private fun obrirCalendariPerSeleccionarData(data: String){
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.textValuedataDiagnosi.setTextColor(colorTextDefault)

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        if(data != ""){
            var parts = data.split("/")
            day =  parts[0].toInt()
            month = parts[1].toInt()-1
            year = parts[2].toInt()
        }

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                binding.valuedataDiagnosi.setText("${dayOfMonth.toString().padStart(2,'0')}/${(monthOfYear+1).toString().padStart(2,'0')}/$year")
            },
            year,
            month,
            day
        )

        dpd.show()
        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun modificarDadesMedOK() {
        alert("Les dades mèdiques del usuari han sigut modificades correctament.","Dades modificades") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun modificarDadesMedNOK() {
        Toast.makeText(this, "Error al modificar les dades mèdiques", Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}
    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesPersonalsNOK() {}
    override fun recuperarDadesMediquesOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesMediquesNOK() {}
    override fun validarContrasenyaOK() {}
    override fun validarContrasenyaNOK() {}
    override fun modificarContrasenyaOK() {}
    override fun modificarContrasenyaNOK() {}

}