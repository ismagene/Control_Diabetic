package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.repository.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentPerfilMedicBinding
import com.ismasoft.controldiabetic.databinding.FragmentPerfilPersonalBinding
import com.ismasoft.controldiabetic.ui.activities.ModificarDadesMedActivity
import com.ismasoft.controldiabetic.ui.activities.ModificarDadesPersActivity
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilMedicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilMedicFragment : Fragment(), PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var bindingFragment: FragmentPerfilMedicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFragment = FragmentPerfilMedicBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        // Recuperem totes les dades de l'usuari
        viewModel.recuperarDadesUsuari(this)

        bindingFragment.buttonModificarDMediques.setOnClickListener(){
            val modificar = Intent(context, ModificarDadesMedActivity::class.java)
            startActivityForResult(modificar, Constants.RETORN_ACTIVITY_OK_CODE)
        }

        return bindingFragment.root
    }

    override fun onResume() {
        super.onResume()
        // Recuperem totes les dades de l'usuari
        viewModel.recuperarDadesMediques(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilMedicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilMedicFragment().apply {}
    }

    override fun recuperarDadesMediquesOK(document: DocumentSnapshot?) {
        // Dades recuperades informades des del VM
    }
    override fun recuperarDadesMediquesNOK() {
        Toast.makeText(context, "Error al recuperar les dades personals", Toast.LENGTH_SHORT).show()
    }
    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesPersonalsNOK() {}
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}
    override fun modificarDadesMedOK() {}
    override fun modificarDadesMedNOK() {}
    override fun validarContrasenyaOK() {}
    override fun validarContrasenyaNOK() {}
    override fun modificarContrasenyaOK() {}
    override fun modificarContrasenyaNOK() {}

}