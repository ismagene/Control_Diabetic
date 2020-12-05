package com.ismasoft.controldiabetic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Control
import com.ismasoft.controldiabetic.data.repository.ControlsRepositoryInterface
import com.ismasoft.controldiabetic.databinding.ActivityRegistre2Binding
import com.ismasoft.controldiabetic.databinding.FragmentControlsBinding
import com.ismasoft.controldiabetic.viewModel.ControlsViewModel
import com.ismasoft.controldiabetic.viewModel.RegistreViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ControlsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlsFragment : Fragment(), ControlsRepositoryInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: FragmentControlsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding = FragmentControlsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.botoHistoric.text = "hola"

        // Comença el fragment amb data binding
        // Recuperem tots els controls de l'usuari.
        viewModel.recuperarLlistaControls(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_controls, container, false).rootView

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
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun afegirControlOK() {}
    override fun afegirControlNOK() {}
    override fun obtenirRangsOK(document: DocumentSnapshot) {}
    override fun obtenirRangsNOK() {}


    override fun llistaControlsOK(llistaControls: List<Control>) {
        binding.ultimControlGlucosa.text = llistaControls.get(0).valorGlucosa.toString()+" mg/dl"

        // AQUI ES QUAN HO TENIM TOT CARREGAT.

    }

    override fun LlistaControlsNOK() {}
    override fun modificarControlOK() {}
    override fun modificarControlNOK() {}
    override fun eliminarControlOK() {}
    override fun eliminarControlNOK() {}
}