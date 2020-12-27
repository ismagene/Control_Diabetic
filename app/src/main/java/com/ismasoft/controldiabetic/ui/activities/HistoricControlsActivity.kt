package com.ismasoft.controldiabetic.ui.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityHistoricControlsBinding
import com.ismasoft.controldiabetic.ui.adapters.ControlsListAdapter
import com.ismasoft.controldiabetic.ui.adapters.ControlsListAdapterInterface
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.utilities.hideKeyboard
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoricControlsActivity : AppCompatActivity(), ControlsListAdapter.ItemClickListener, ControlsListAdapterInterface,
    ControlsRepositoryInterface {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityHistoricControlsBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ControlsListAdapter

    var llistaControls = ArrayList<ControlAmbId>()
    var llistaControlsFiltrats = ArrayList<ControlAmbId>()
    var dataFiltreInici: Date =
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1900 00:00:00")
    var dataFiltreFi: Date =
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2900 00:00:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var objetoIntent : Intent = intent

        // Per tornar endarrera
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Recuperem els valors enviats del fragment de controls
        llistaControls = objetoIntent.extras?.get("llistaControls") as ArrayList<ControlAmbId>

        // Ordenem els controls.
        llistaControls.sortWith(kotlin.Comparator { o1, o2 ->
            var date1 = o1.dataControl
            var date2 = o2.dataControl
            date2?.compareTo(date1)!! // Comparamem les dates
        })
        llistaControlsFiltrats = llistaControls

        binding = ActivityHistoricControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        recyclerView = binding.recyclerViewControls
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Posem els valors de la llista de controls
        adapter = ControlsListAdapter(this, llistaControls, this)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        /* Si tenim obert el teclat virtual s'amaga automaticament quan apretem el botó */
        hideKeyboard(this)

        binding.diaFiltreInici.inputType = InputType.TYPE_NULL
        binding.diaFiltreInici.setOnClickListener {
            obrirCalendariPerSeleccionarData(binding.diaFiltreInici.text.toString())
        }
        binding.diaFiltreInici.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                obrirCalendariPerSeleccionarData(binding.diaFiltreInici.text.toString())
            }
        }
        binding.diaFiltreFi.inputType = InputType.TYPE_NULL
        binding.diaFiltreFi.setOnClickListener {
            obrirCalendariPerSeleccionarDataFi(binding.diaFiltreFi.text.toString())
        }
        binding.diaFiltreFi.setOnFocusChangeListener(){ _, hasFocus->
            if (hasFocus) {
                obrirCalendariPerSeleccionarDataFi(binding.diaFiltreFi.text.toString())
            }
        }
        binding.eliminarFiltreIni.setOnClickListener(){
            binding.diaFiltreInici.setText("")
        }
        binding.eliminarFiltreFi.setOnClickListener(){
            binding.diaFiltreFi.setText("")
        }

        binding.filtrarControls.setOnClickListener(){

            if(validarFiltrar()) {

                llistaControlsFiltrats = ArrayList<ControlAmbId>()

                if (!binding.diaFiltreInici.text.toString().equals("")) {
                    val date = binding.diaFiltreInici.text.toString()
                    dataFiltreInici =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("$date 00:00:00")
                } else {
                    dataFiltreInici =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/1900 00:00:00")
                }

                if (!binding.diaFiltreFi.text.toString().equals("")) {
                    val date = binding.diaFiltreFi.text.toString()
                    dataFiltreFi = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("$date 23:59:59")
                } else {
                    dataFiltreFi =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2900 00:00:00")
                }

                for (control in llistaControls) {
                    if (dataFiltreInici.before(control.dataControl) && dataFiltreFi.after(control.dataControl)) {
                        llistaControlsFiltrats.add(control)
                    }
                }
                viewModel.hiHanControls(llistaControls, llistaControlsFiltrats, this)

                adapter = ControlsListAdapter(this, llistaControlsFiltrats, this)
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
                adapter.notifyItemRangeChanged(0, llistaControlsFiltrats.size)
                adapter.notifyDataSetChanged()

            }

        }

        binding.botoEnviarHistoric.setOnClickListener(){

            if(validarEnviarHistoric()) {
                val historic = Intent(this, EnviarHistoricActivity::class.java)
                val extras = Bundle()
                extras.putSerializable("llistaControlsFiltrats", llistaControlsFiltrats)
                historic.putExtras(extras)
                startActivityForResult(historic, Constants.RETORN_ACTIVITY_OK_CODE)
            }
        }

        binding.tornarMenu.setOnClickListener(){
            hideKeyboard(this)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.recuperarLlistaControls(this)
        viewModel.hiHanControls(llistaControls, llistaControlsFiltrats, this)
        adapter = ControlsListAdapter(this, llistaControlsFiltrats, this)
        adapter.notifyDataSetChanged()
    }

    private fun validarEnviarHistoric(): Boolean {
        if(llistaControlsFiltrats.size<=0){
            Toast.makeText(this, "No hi ha cap control filtrat per poder enviar. ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validarFiltrar(): Boolean {
        if(llistaControls.size<=0){
            Toast.makeText(this, "No hi ha cap control per filtrar. ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun obrirCalendariPerSeleccionarData(data :String){

        var c = Calendar.getInstance()
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
                binding.diaFiltreInici.setText(
                    "${dayOfMonth.toString().padStart(2, '0')}/${
                        (monthOfYear + 1).toString().padStart(
                            2,
                            '0'
                        )
                    }/$year"
                )
            },
            year,
            month,
            day
        )
        dpd.show()

    }

    private fun obrirCalendariPerSeleccionarDataFi(data :String){

        var c = Calendar.getInstance()
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
                binding.diaFiltreFi.setText(
                    "${dayOfMonth.toString().padStart(2, '0')}/${
                        (monthOfYear + 1).toString().padStart(
                            2,
                            '0'
                        )
                    }/$year"
                )
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun eliminarControlOK(position: Int) {}
    override fun eliminarControlNOK() {}
    override fun hihaControls() {
        adapter = ControlsListAdapter(this, llistaControls, this)
        adapter.notifyDataSetChanged()
    }
    override fun noHihaControls() {}
    override fun hihaControlsFiltrats() {
        adapter = ControlsListAdapter(this, llistaControls, this)
        adapter.notifyDataSetChanged()
    }

    override fun noHihaControlsFiltrats() {}

    fun senseControls(){
        viewModel.senseControls()
    }

    override fun afegirControlOK() {}
    override fun afegirControlNOK() {}
    override fun obtenirRangsOK(document: DocumentSnapshot) {}

    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(llistaRecuperada: ArrayList<ControlAmbId>) {

        // Resultat per pantalla
        llistaControls = llistaRecuperada
        // Ordenem els controls.
        llistaControls.sortWith(kotlin.Comparator { o1, o2 ->
            var date1 = o1.dataControl
            var date2 = o2.dataControl
            date2?.compareTo(date1)!! // Comparamem les dates
        })

        llistaControlsFiltrats = ArrayList<ControlAmbId>()

        for (control in llistaControls) {
            if (dataFiltreInici.before(control.dataControl) && dataFiltreFi.after(control.dataControl)) {
                llistaControlsFiltrats.add(control)
            }
        }

        adapter = ControlsListAdapter(this, llistaControlsFiltrats, this)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        adapter.notifyItemRangeChanged(0, llistaControlsFiltrats.size)
        adapter.notifyDataSetChanged()

    }
    override fun LlistaControlsNOK() {}
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun onItemClick(view: View, position: Int) {}

}