package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentVisitesBinding
import com.ismasoft.controldiabetic.ui.activities.ModificarAlarmaActivity
import com.ismasoft.controldiabetic.ui.activities.ModificarVisitaActivity
import com.ismasoft.controldiabetic.ui.adapters.AlarmesListAdapter
import com.ismasoft.controldiabetic.ui.adapters.VisitesListAdapter
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [VisitesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisitesFragment : Fragment(), VisitesListAdapter.ItemClickListener, VisitesRepositoryInterface {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var bindingFragment: FragmentVisitesBinding

    val llistaVisites = ArrayList<VisitaAmbId>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VisitesListAdapter

    private lateinit var visitaVigent : VisitaAmbId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFragment = FragmentVisitesBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        recyclerView = bindingFragment.recyclerViewVisites
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { VisitesListAdapter(it, llistaVisites) }!!
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        bindingFragment.botoModificarVisita.setOnClickListener(){
            val modificarVisita = Intent(context, ModificarVisitaActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("visitaModificar",visitaVigent)
            modificarVisita.putExtras(extras)
            this.startActivity(modificarVisita)
        }

        bindingFragment.botoEliminarVisita.setOnClickListener(){

        }

        return bindingFragment.root

    }

    override fun onResume() {
        super.onResume()
        viewModel.recuperarLlistaVisites(this)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VisitesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VisitesFragment().apply {
            }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(context, "Clicat element $position", Toast.LENGTH_SHORT).show()
    }

    override fun afegirVisitaOK() {
        TODO("Not yet implemented")
    }

    override fun afegirVisitaNOK() {
        TODO("Not yet implemented")
    }

    override fun existeixVisitaVigent() {
        TODO("Not yet implemented")
    }

    override fun errorAlConsultarVisitaVigent() {
        TODO("Not yet implemented")
    }

    override fun noExisteixVisitaVigent() {
        TODO("Not yet implemented")
    }

    override fun llistaVisitesOK(llistaVisites: ArrayList<VisitaAmbId>) {

        if(viewModel.ambProximaVisita.value == true){
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            var dataString = sdf.format(viewModel.visitaVigent.value?.dataVisita)
            bindingFragment.dataVisita.text = dataString.toString()
            bindingFragment.motiuVisita.text = viewModel.visitaVigent.value?.motiu.toString()
            visitaVigent.idVisita = viewModel.visitaVigent.value?.idVisita
            visitaVigent.dataVisita = viewModel.visitaVigent.value?.dataVisita
            visitaVigent.motiu = viewModel.visitaVigent.value?.motiu
        }

        recyclerView.adapter = context?.let { VisitesListAdapter(it, llistaVisites) }!!
        adapter.notifyDataSetChanged()
    }

    override fun llistaVisitesNOK() {}
    override fun modificarVisitaOK() {}
    override fun modificarVisitaNOK() {}
    override fun eliminarVisitaOK() {}
    override fun eliminarVisitaNOK() {}
}