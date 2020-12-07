package com.ismasoft.controldiabetic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.databinding.FragmentVisitesBinding
import com.ismasoft.controldiabetic.viewModel.VisitesViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [VisitesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisitesFragment : Fragment() {

    private lateinit var viewModel: VisitesViewModel
    private lateinit var bindingFragment: FragmentVisitesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentVisitesBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get()

//        bindingFragment.viewModel = viewModel
        bindingFragment.lifecycleOwner = this


        return bindingFragment.root
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
}