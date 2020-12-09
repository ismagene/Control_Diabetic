package com.ismasoft.controldiabetic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.data.model.Alarma
import com.ismasoft.controldiabetic.data.model.AlarmaAmbId
import com.ismasoft.controldiabetic.data.repository.AlarmesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentAlarmesBinding
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapter
import com.ismasoft.controldiabetic.viewModel.AlarmesViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmesFragment : Fragment(), AlarmesListAdapter.ItemClickListener, AlarmesRepositoryInterface {

    private lateinit var viewModel: AlarmesViewModel
    private lateinit var bindingFragment: FragmentAlarmesBinding

    val llistaVisites = HashMap<String, Alarma>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlarmesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bindingFragment = FragmentAlarmesBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        recyclerView = bindingFragment.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { AlarmesListAdapter(it, ArrayList<AlarmaAmbId>()) }!!
        adapter.setClickListener(this)
        recyclerView.adapter = adapter

        return bindingFragment.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.recuperarLlistaAlarmes(this)

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
1                }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(context, "Clicat element $position", Toast.LENGTH_SHORT).show()
    }

    override fun afegirAlarmaOK() {}
    override fun afegirAlarmaNOK() {}
    override fun jaExisteixAlarma() {}
    override fun noExisteixAlarma() {}
    override fun modificarAlarmaOK() {}
    override fun modificarAlarmaNOK() {}

    override fun llistaAlarmesOK(llistaAlarmes: ArrayList<AlarmaAmbId>) {

        recyclerView.adapter = context?.let { AlarmesListAdapter(it, llistaAlarmes) }!!
        adapter.notifyDataSetChanged()

    }

    override fun llistaAlarmesNOK() {}
}