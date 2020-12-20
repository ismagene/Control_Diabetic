package com.ismasoft.controldiabetic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentAlarmesBinding
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapter
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmesFragment : Fragment(), AlarmesListAdapter.ItemClickListener,
    AlarmesRepositoryInterface {

    private lateinit var viewModel: AlarmesViewModel
    private lateinit var bindingFragment: FragmentAlarmesBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlarmesListAdapter

    private var llistaAlarmesGlobal = ArrayList<AlarmaAmbId>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bindingFragment = FragmentAlarmesBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        recyclerView = bindingFragment.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { AlarmesListAdapter(it, ArrayList<AlarmaAmbId>(),this)}!!
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        return bindingFragment.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.recuperarLlistaAlarmes(this)
        adapter.notifyDataSetChanged()
        if(adapter.senseAlarmes.value == true){
            viewModel.noQuedenAlarmes()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmesFragment().apply {
        }
    }

    override fun onItemClick(view: View, position: Int) {
//        Toast.makeText(context, "Clicat element $position", Toast.LENGTH_SHORT).show()
    }

    override fun afegirAlarmaOK(idAlarmaManager: Int?) {}
    override fun afegirAlarmaNOK() {}
    override fun jaExisteixAlarma() {}
    override fun noExisteixAlarma() {}
    override fun modificarAlarmaOK(idAlarmaManager: Int?) {}
    override fun modificarAlarmaNOK() {}

    override fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>) {

        llistaAlarmes.sortWith(kotlin.Comparator { o1, o2 ->
            var obj1 = o1 as AlarmaAmbId
            var obj2 = o2 as AlarmaAmbId
            var isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date1 = Date()
            var date2 = Date()
            try {
                // Convertimos las cadenas en date
                date1 = isoFormat.parse("2000-01-01 "+obj1.horaAlarma.toString()+":00") as Date
                date2 = isoFormat.parse("2000-01-01 "+obj2.horaAlarma.toString()+":00") as Date
            } catch (ex: ParseException) {
                Logger.getLogger("Error al converitr les dates de les alarmes")
            }
            date1.compareTo(date2) // Comparamos las fechas
        })

        llistaAlarmesGlobal = llistaAlarmes

        recyclerView.adapter = context?.let { AlarmesListAdapter(it, llistaAlarmes, this) }!!
        adapter.setClickListener(this)
        adapter.notifyItemRangeChanged(0,llistaAlarmes.size)
        adapter.notifyDataSetChanged()

    }

    fun noAlarmes(){
        parentFragmentManager.beginTransaction().detach(this).commitNow()
        parentFragmentManager.beginTransaction().attach(this).commitNow()
    }

    override fun llistaAlarmesNOK() {}
    override fun recuperarIdAlarmaNovaOK(idAlarma: Int) { }
    override fun recuperarIdAlarmaNovaNOK() {}
}
