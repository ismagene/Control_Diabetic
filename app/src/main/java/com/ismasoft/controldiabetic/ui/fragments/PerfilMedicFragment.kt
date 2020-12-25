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
import com.ismasoft.controldiabetic.data.model.User
import com.ismasoft.controldiabetic.data.repository.interfaces.PerfilRepositoryInterface
import com.ismasoft.controldiabetic.databinding.FragmentPerfilMedicBinding
import com.ismasoft.controldiabetic.ui.activities.ModificarDadesMedActivity
import com.ismasoft.controldiabetic.utilities.Constants
import com.ismasoft.controldiabetic.viewModel.PerfilViewModel
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilMedicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilMedicFragment : Fragment(), PerfilRepositoryInterface {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var bindingFragment: FragmentPerfilMedicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingFragment = FragmentPerfilMedicBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this

        bindingFragment.buttonModificarDMediques.setOnClickListener(){
            var usuari = recuperarDadesUsuari()
            val modificar = Intent(context, ModificarDadesMedActivity::class.java)
            val extras = Bundle()
            extras.putSerializable("usuariPerfilMed",usuari)
            modificar.putExtras(extras)
            startActivityForResult(modificar, Constants.RETORN_ACTIVITY_OK_CODE)
        }

        return bindingFragment.root
    }

    private fun recuperarDadesUsuari(): User {

        val centre : String? = viewModel.valueNomDelCentre.value.toString()
        val poblacioCentre: String? = viewModel.poblacioDelCentre.value.toString()
        val nomDelMetge: String? = viewModel.nomDelMetge.value.toString()
        val correuElectronicMetge: String? = viewModel.correuElectronicMetge.value.toString()
        val tipuDiabetis: String? = viewModel.tipusDiabetis.value.toString()
        val date: String? = viewModel.dataDiagnosi.value.toString()
        val dataDiagnosi = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date 00:00")
        val glucosaBaixaString: String? = viewModel.glucosaBaixa.value.toString()
        var parts = glucosaBaixaString!!.split(" ")
        var glucosaBaixa = parts[0].toInt()
        val glucosaAltaString: String? = viewModel.glucosaAlta.value.toString()
        parts = glucosaAltaString!!.split(" ")
        var glucosaAlta = parts[0].toInt()
        val glucosaMoltBaixaString: String? = viewModel.glucosaMoltBaixa.value.toString()
        parts = glucosaMoltBaixaString!!.split(" ")
        var glucosaMoltBaixa = parts[0].toInt()
        val glucosaMoltAltaString: String? = viewModel.glucosaMoltAlta.value.toString()
        parts = glucosaMoltAltaString!!.split(" ")
        var glucosaMoltAlta = parts[0].toInt()
        val glucosaBaixaDAString: String? = viewModel.glucosaBaixaDA.value.toString()
        parts = glucosaBaixaDAString!!.split(" ")
        var glucosaBaixaDA = parts[0].toInt()
        val glucosaAltaDAString: String? = viewModel.glucosaAltaDA.value.toString()
        parts = glucosaAltaDAString!!.split(" ")
        var glucosaAltaDA = parts[0].toInt()

        return User(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            centre,
            poblacioCentre,
            nomDelMetge,
            correuElectronicMetge,
            tipuDiabetis,
            dataDiagnosi,
            glucosaMoltBaixa,
            glucosaBaixa,
            glucosaAlta,
            glucosaMoltAlta,
            glucosaBaixaDA,
            glucosaAltaDA
        )

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