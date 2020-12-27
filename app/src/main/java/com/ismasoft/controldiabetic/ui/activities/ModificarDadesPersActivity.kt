package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.interfaces.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityModificarDadesPersBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class ModificarDadesPersActivity : AppCompatActivity() , PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var binding: ActivityModificarDadesPersBinding

    private var constants: Constants = Constants
    private lateinit var colorTextDefault : ColorStateList

    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModificarDadesPersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del text per defecte
        colorTextDefault = binding.textValueNom.textColors

        // Recuperem info del usuari
        var objetoIntent : Intent = intent
        var usuari : User = objetoIntent.extras?.get("usuariPerfil") as User

        // Opcións del spiner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genere,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.valueGenere.adapter = adapter

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.mostrarDadesPersonals(usuari)

        if(viewModel.genere.value.equals("Home")) {
            binding.valueGenere.setSelection(1)
        }else if(viewModel.genere.value.equals("Dona")) {
            binding.valueGenere.setSelection(2)
        }else{
            binding.valueGenere.setSelection(3)
        }

        binding.buttonModificarDadesPers.setOnClickListener(){
            hideKeyboard(this)
            // Per modificar-ho
            if(validarEntrada()){
                var usuariAModificar = inicialitzarUsuari()
                viewModel.modificarDadesPers(usuariAModificar, this)
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
            }

        }

        binding.cancelarGuardarDadesPers.setOnClickListener(){
            hideKeyboard(this)
            finish()
        }

        /* Recuperem els valors de Hint i de Color del text si han estat marcats com error */
        binding.valueNom.setOnClickListener(){
            binding.textValueNom.setTextColor(colorTextDefault)
        }
        binding.valueNom.setOnFocusChangeListener { _, hasFocus ->
            if(!primerOnCreate) {
                if(hasFocus) {
                    binding.textValueNom.setTextColor(colorTextDefault)
                }
            }else{
                /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
                hideKeyboard(this)
                primerOnCreate=false
            }
        }
        binding.valueCognom.setOnClickListener(){
            binding.textValueCognom.setTextColor(colorTextDefault)
        }
        binding.valueCognom.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValueCognom.setTextColor(colorTextDefault)
        }
        binding.valueCognom2.setOnClickListener(){
            binding.textValueCognom2.setTextColor(colorTextDefault)
        }
        binding.valueCognom2.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValueCognom2.setTextColor(colorTextDefault)
        }
        binding.valueDataNaixament.inputType = InputType.TYPE_NULL
        binding.valueDataNaixament.setOnFocusChangeListener { _, hasFocus ->
            hideKeyboard(this)
            if(hasFocus) {
                obrirCalendariPerSeleccionarData(binding.valueDataNaixament.text.toString())
            }
        }
        binding.valueDataNaixament.setOnClickListener {
            hideKeyboard(this)
            obrirCalendariPerSeleccionarData(binding.valueDataNaixament.text.toString())
        }
        binding.valueGenere.setOnTouchListener { view, motionEvent ->
            binding.textspinerGenere.setTextColor(colorTextDefault)
            view.performClick()
            return@setOnTouchListener true
        }
        binding.valuePes.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValuePes.setTextColor(colorTextDefault)
        }
        binding.valuePes.setOnClickListener() {
            binding.textValuePes.setTextColor(colorTextDefault)
        }
        binding.valueAltura.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) binding.textValueAltura.setTextColor(colorTextDefault)
        }
        binding.valueAltura.setOnClickListener() {
            binding.textValueAltura.setTextColor(colorTextDefault)
        }

        binding.valueCorreuElectronic.setOnClickListener(){
            Toast.makeText(this, "El correu electrònic no es pot modificar", Toast.LENGTH_SHORT).show()
        }


    }

    private fun validarEntrada(): Boolean {
        if (binding.valueNom.text == null || binding.valueNom.text.toString() == "") {
            binding.textValueNom.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El nom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueCognom.text == null || binding.valueCognom.text.toString() == "") {
            binding.textValueCognom.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueCognom2.text == null || binding.valueCognom2.text.toString() == "") {
            binding.textValueCognom2.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El segon cognom és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueDataNaixament.text == null || binding.valueDataNaixament.text.toString() == "") {
            binding.textValueDataNaixament.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        // Validem que la data no sigui superior o igual a la del dia
        val dataIntroduida = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("${binding.valueDataNaixament.text.toString()} 23:59")
        val dataActual = Date()
        if(dataIntroduida.after(dataActual))
        {
            binding.textValueDataNaixament.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "La data de naixement no pot ser igual o superior a l'actual'", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueGenere.selectedItem == constants.OPCIO_DEFECTE_SPINER) {
            binding.textspinerGenere.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El genere és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valuePes.text == null || binding.valuePes.text.toString() == "") {
            binding.textValuePes.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El Pes és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valuePes.text.toString().toInt() <= 0) {
            binding.textValuePes.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "El Pes no pot ser 0", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueAltura.text == null || binding.valueAltura.text.toString() == "") {
            binding.textValueAltura.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'altura és un camp obligatori", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.valueAltura.text.toString().toInt() <= 0) {
            binding.textValueAltura.setTextColor(constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "L'alçada no pot ser 0", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun obrirCalendariPerSeleccionarData(data: String){
        binding.textValueDataNaixament.setTextColor(colorTextDefault)

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
                binding.valueDataNaixament.setText("${dayOfMonth.toString().padStart(2,'0')}/${(monthOfYear+1).toString().padStart(2,'0')}/$year")
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun inicialitzarUsuari(): User {

        val nom : String = binding.valueNom.text.toString()
        val cognom: String = binding.valueCognom.text.toString()
        val cognom2: String = binding.valueCognom2.text.toString()
        val date: String = binding.valueDataNaixament.text.toString()
        val dataNaixament = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date 00:00")
        val genere: String = binding.valueGenere.selectedItem.toString()
        val pesString: String = binding.valuePes.text.toString()
        var parts = pesString!!.split(" ")
        var pes =  parts[0].toInt()
        val pesNumeric = pes.toInt()
        val alturaString: String = binding.valueAltura.text.toString()
        parts = alturaString!!.split(" ")
        var altura =  parts[0].toInt()
        val alturaNumeric = altura.toInt()

        return User(
            nom,
            cognom,
            cognom2,
            dataNaixament,
            null,
            genere,
            pesNumeric,
            alturaNumeric,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun modificarDadesPersOK() {
        alert("Les dades del usuari han sigut modificades correctament.","Dades modificades") {
            cancellable(false)
            positiveButton("Continuar") {
                setResult(RESULT_OK)
                finish()
            }
        }.show()
    }
    override fun modificarDadesPersNOK() {
        Toast.makeText(this, "Error al modificar les dades personals", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesPersonalsNOK() {}
    override fun recuperarDadesMediquesOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesMediquesNOK() {}
    override fun modificarDadesMedOK() {}
    override fun modificarDadesMedNOK() {}
    override fun validarContrasenyaOK() {}
    override fun validarContrasenyaNOK() {}
    override fun modificarContrasenyaOK() {}
    override fun modificarContrasenyaNOK() {}

}