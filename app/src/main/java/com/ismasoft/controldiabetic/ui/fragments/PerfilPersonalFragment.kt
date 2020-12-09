package com.ismasoft.controldiabetic.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilPersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilPersonalFragment : Fragment(), PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var bindingFragment: FragmentPerfilPersonalBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentPerfilPersonalBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

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
            activity?.finish()

            /* Preferences per guardar dades en un xml local */
            preferences = context?.applicationContext!!.getSharedPreferences("ControlDiabetic",
                AppCompatActivity.MODE_PRIVATE
            )
            editor = preferences.edit()

            editor.putString("checkGuardat", null)
            editor.apply()
            editor.clear()

        }

        return bindingFragment.root
    }

    override fun onResume() {
        super.onResume()
        // Recuperem totes les dades de l'usuari
        viewModel.recuperarDadesUsuari(this)
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
            PerfilPersonalFragment().apply {}
    }

    override fun recuperarDadesPersonalsOK(document: DocumentSnapshot?) {
        // Dades recuperades informades des del VM

    }
    override fun recuperarDadesPersonalsNOK() {
        Toast.makeText(context, "Error al recuperar les dades personals", Toast.LENGTH_SHORT).show()
    }
    override fun recuperarDadesMediquesOK(document: DocumentSnapshot?) {}
    override fun recuperarDadesMediquesNOK() {}
    override fun modificarDadesPersOK() {}
    override fun modificarDadesPersNOK() {}
    override fun modificarDadesMedOK() {}
    override fun modificarDadesMedNOK() {}
    override fun validarContrasenyaOK() {}
    override fun validarContrasenyaNOK() {}
    override fun modificarContrasenyaOK() {}
    override fun modificarContrasenyaNOK() {}
}