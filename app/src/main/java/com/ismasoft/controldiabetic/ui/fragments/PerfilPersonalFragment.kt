package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.firestore.DocumentSnapshot
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.repository.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentPerfilPersonalBinding
import com.ismasoft.controldiabetic.ui.activities.HistoricControlsActivity
import com.ismasoft.controldiabetic.ui.activities.LoginActivity
import com.ismasoft.controldiabetic.ui.activities.ModificarDadesPersActivity
import com.ismasoft.controldiabetic.ui.activities.ModificarPasswordActivity
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilPersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilPersonalFragment : Fragment(), PerfilRepositoryInterface {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var viewModel: PerfilViewModel
    private lateinit var bindingFragment: FragmentPerfilPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentPerfilPersonalBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        // Recuperem totes les dades de l'usuari
        viewModel.recuperarDadesUsuari(this)

        bindingFragment.buttonModificar.setOnClickListener(){
            val modificar = Intent(context, ModificarDadesPersActivity::class.java)
            startActivityForResult(modificar, Constants.RETORN_ACTIVITY_OK_CODE)
        }

        bindingFragment.buttonModificarPass.setOnClickListener(){
            val modificarPass = Intent(context, ModificarPasswordActivity::class.java)
            startActivityForResult(modificarPass, Constants.RETORN_ACTIVITY_OK_CODE)
        }

        bindingFragment.tancarSessio.setOnClickListener(){
            viewModel.tancarSessio()
            val login = Intent(context, LoginActivity::class.java)
            startActivity(login)
            // OJO TANCAR DESDE DEL FRAGMENT
        }

        return bindingFragment.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilPersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilPersonalFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }

    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {
        // Dades recuperades informades des del VM

    }
    override fun recuperarDadesPersonalsNOK() {}
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}
    override fun validarContrasenyaOK() {}
    override fun validarContrasenyaNOK() {}
    override fun modificarContrasenyaOK() {}
    override fun modificarContrasenyaNOK() {}
}