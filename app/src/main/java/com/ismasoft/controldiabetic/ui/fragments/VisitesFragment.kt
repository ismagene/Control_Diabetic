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
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.data.model.Visita
import com.ismasoft.controldiabetic.data.model.VisitaAmbId
import com.ismasoft.controldiabetic.databinding.FragmentAlarmesBinding
import com.ismasoft.controldiabetic.databinding.FragmentVisitesBinding
import com.ismasoft.controldiabetic.ui.adapters.VisitesListAdapter
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [VisitesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisitesFragment : Fragment(), VisitesListAdapter.ItemClickListener {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var bindingFragment: FragmentVisitesBinding

    val llistaVisites = ArrayList<VisitaAmbId>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VisitesListAdapter

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

        recyclerView = bindingFragment.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = context?.let { VisitesListAdapter(it, llistaVisites) }!!
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        return bindingFragment.root



//        bindingFragment = FragmentVisitesBinding.inflate(layoutInflater, container, false)
//        viewModel = ViewModelProvider(this).get()
//
////        bindingFragment.viewModel = viewModel
//        bindingFragment.lifecycleOwner = this
//
////        val view = inflater.inflate(R.layout.fragment_visites, container, false)
//
////        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView = bindingFragment.recyclerView
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = context?.let { VisitesListAdapter(it, llistaVisites) }!!
//        adapter.setClickListener(this)
//        recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()
//
//        return view
//        return bindingFragment.root
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
}