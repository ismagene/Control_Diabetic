package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.VisitesRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentVisitesBinding
import com.ismasoft.controldiabetic.ui.activities.ModificarVisitaActivity
import com.ismasoft.controldiabetic.ui.adapters.VisitesListAdapter
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import org.jetbrains.anko.alert
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

    private var visitaVigent = VisitaAmbId()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            extras.putSerializable("visitaModificar", visitaVigent)
            modificarVisita.putExtras(extras)
            this.startActivity(modificarVisita)
        }

        bindingFragment.botoEliminarVisita.setOnClickListener(){
            requireContext().alert("Segur que voleu eliminar la visita?", "Eliminar visita") {
                cancellable(false)
                positiveButton("Confirmar") {
                    viewModel.eliminarVisita(visitaVigent.idVisita.toString(), this@VisitesFragment)
                }
                negativeButton("Cancelar"){
                    // Nothing to do
                }
            }.show()

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

    override fun onItemClick(view: View, position: Int) {}
    override fun afegirVisitaOK() {}
    override fun afegirVisitaNOK() {}
    override fun existeixVisitaVigent() {}
    override fun errorAlConsultarVisitaVigent() {}
    override fun noExisteixVisitaVigent() {}

    override fun llistaVisitesOK(llistaVisites: ArrayList<VisitaAmbId>) {

        if(viewModel.ambProximaVisita.value == true){
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val dataString = sdf.format(viewModel.visitaVigent.value?.dataVisita)
            val parts = dataString.split(" ")
            val dia =  parts[0]
            val hora = parts[1]

            bindingFragment.dataVisita.text = dia
            bindingFragment.horaVisita.text = hora
            bindingFragment.motiuVisita.text = viewModel.visitaVigent.value?.motiu.toString()

            visitaVigent.idVisita = viewModel.visitaVigent.value?.idVisita.toString()
            visitaVigent.dataVisita = viewModel.visitaVigent.value?.dataVisita
            visitaVigent.motiu = viewModel.visitaVigent.value?.motiu
        }

        // Ordenem les visites de mes recent a mes antiga.
        llistaVisites.sortWith(kotlin.Comparator { o1, o2 ->
            val date1 = o1.dataVisita
            val date2 = o2.dataVisita
            date2?.compareTo(date1)!! // Comparamem les dates
        })

        recyclerView.adapter = context?.let { VisitesListAdapter(it, llistaVisites) }!!
        adapter.notifyDataSetChanged()
    }

    override fun llistaVisitesNOK() {}
    override fun modificarVisitaOK() {}
    override fun modificarVisitaNOK() {}
    override fun eliminarVisitaOK() {}
    override fun eliminarVisitaNOK() {
        Toast.makeText(context, "Error al eliminar la visita", Toast.LENGTH_SHORT).show()
    }
}