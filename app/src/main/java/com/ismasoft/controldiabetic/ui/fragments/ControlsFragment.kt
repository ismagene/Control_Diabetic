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
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.ControlAmbId
import com.ismasoft.controldiabetic.data.repository.interfaces.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentControlsBinding
import com.ismasoft.controldiabetic.ui.activities.HistoricControlsActivity
import com.ismasoft.controldiabetic.utilities.Constants.RETORN_ACTIVITY_OK_CODE
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ControlsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ControlsFragment : Fragment(), ControlsRepositoryInterface {

    private lateinit var viewModel: ControlsViewModel
    private lateinit var bindingFragment: FragmentControlsBinding

    private var llistaControlsGlobal : ArrayList<ControlAmbId> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bindingFragment = FragmentControlsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        bindingFragment.botoHistoric.setOnClickListener(){

            val historic = Intent(context, HistoricControlsActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("llistaControls", llistaControlsGlobal)
            historic.putExtras(extras)
            startActivityForResult(historic, RETORN_ACTIVITY_OK_CODE)

        }

        return bindingFragment.root
    }

    // Es carregaràn els controls cada vegada que obrim la pestanya per si hi han hagut canvis
    override fun onResume() {
        super.onResume()

        // Comença el fragment amb data binding
        // Recuperem tots els controls de l'usuari.
        viewModel.recuperarLlistaControls(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ControlsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ControlsFragment().apply {}
    }

    override fun afegirControlOK() {}
    override fun afegirControlNOK() {}
    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(llistaControls: ArrayList<ControlAmbId>) {
        // Dades recuperades informades des del VM
        llistaControlsGlobal = llistaControls
    }
    override fun LlistaControlsNOK() {
        // Enviar Toast al Activity?
        Toast.makeText(context, "Error al recuperar les dades de controls", Toast.LENGTH_SHORT).show()
    }
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
}
