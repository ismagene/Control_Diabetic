package com.ismasoft.controldiabetic.ui.activities

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityAfegirVisitaBinding
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AfegirVisitaActivity : AppCompatActivity() , VisitesRepositoryInterface {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var binding: ActivityAfegirVisitaBinding

    private lateinit var colorTextDefault : ColorStateList
    private var primerOnCreate : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfegirVisitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Guardem els colors del hint i del text per defecte
        colorTextDefault = binding.diaVisita.textColors

        // dia i hora per defecte.
        dataIHoraPerDefecte()

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.guardarVisita.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)

            if(validarEntrada()){
                var alarma = inicialitzarVisita(binding)
                // Primer comprovarem que poguem insertar una nova visita
                viewModel.onButtonGuardarVisita( alarma,this)
            }

        }

        binding.cancelarGuardarVisita.setOnClickListener(){
            /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
            hideKeyboard(this)
            finish()
        }

    }

    private fun validarEntrada(): Boolean {
        if(binding.horaVisita.text == null || binding.horaVisita.text.toString() == ""){
            binding.textHoraVisita.setTextColor(Constants.COLOR_ERROR_FALTA_CAMP)
            Toast.makeText(this, "Falta introduir el valor de glucosa", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun inicialitzarVisita(binding: ActivityAfegirVisitaBinding): Visita {

        val date = binding.diaVisita.text.toString()
        val hora = binding.horaVisita.text.toString()
        val dataHoraVisita  = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $hora")
        val motiu = binding.motiuVisita.toString()

        return Visita(dataHoraVisita, motiu)
    }

    private fun dataIHoraPerDefecte() {
        val cal = Calendar.getInstance()
        var hora = cal.get(Calendar.HOUR_OF_DAY)
        var minuts = cal.get(Calendar.MINUTE)
        var horaString = hora.toString().padStart(2, '0')
        binding.horaVisita.setText(
            "${hora.toString().padStart(2, '0')}:${
                minuts.toString().padStart(
                    2,
                    '0'
                )
            }"
        )
    }

    override fun afegirVisitaOK() {}
    override fun afegirVisitaNOK() {}
    override fun existeixVisitaVigent() {

    }

    override fun noExisteixVisitaVigent() {

    }

    override fun llistaVisitesOK(document: ArrayList<VisitaAmbId>) {

    }

    override fun llistaVisitesNOK() {

    }

    override fun modificarVisitaOK() {}
    override fun modificarVisitaNOK() {}
    override fun eliminarVisitaOK() {}
    override fun eliminarVisitaNOK() {}


}