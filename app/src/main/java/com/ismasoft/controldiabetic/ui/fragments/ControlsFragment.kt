package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentControlsBinding
import com.ismasoft.controldiabetic.ui.activities.HistoricControlsActivity
import com.ismasoft.controldiabetic.utilities.Constants.REGISTRE_2_CODE
import com.ismasoft.controldiabetic.utilities.Constants.RETORN_ACTIVITY_OK_CODE
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ControlsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ControlsFragment : Fragment(), ControlsRepositoryInterface {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var viewModel: ControlsViewModel
    private lateinit var bindingFragment: FragmentControlsBinding

    private lateinit var _llistaControls: MutableLiveData<HashMap<String, Control>>
    val llistaControls2 : LiveData<HashMap<String, Control>> get() = _llistaControls

    private lateinit var llistaControlsGlobal : HashMap<String, Control>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentControlsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        // Comença el fragment amb data binding
        // Recuperem tots els controls de l'usuari.
//        viewModel.recuperarLlistaControls(this)

        bindingFragment.botoHistoric.setOnClickListener(){

            val historic = Intent(context, HistoricControlsActivity::class.java)
//            val extras = Bundle()
//            extras.putSerializable("HashMap", llistaControlsGlobal)
//            historic.putExtras(extras)
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
                ControlsFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }

    override fun afegirControlOK() {}
    override fun afegirControlNOK() {}
    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}
    override fun llistaControlsOK(llistaControls: HashMap<String, Control>) {
        // Dades recuperades informades des del VM
        llistaControlsGlobal = llistaControls
//        _llistaControls.value = llistaControls
    }
    override fun LlistaControlsNOK() {
        // Enviar Toast al Activity?
        Toast.makeText(context, "Error al recuperar les dades de controls", Toast.LENGTH_SHORT).show()
    }
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun eliminarControlOK() {}
    override fun eliminarControlNOK() {}
}
