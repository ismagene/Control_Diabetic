package com.ismasoft.controldiabetic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ismasoft.controldiabetic.ui.adapters.ProfilePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.ui.activities.MenuPrincipalActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    private lateinit var tabHost: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var profilePagerAdapter: ProfilePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_perfil, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniVars()
        viewPagerListener()
    }

    private fun iniVars() {

        tabHost = mView.findViewById(R.id.tablayout)
        viewPager = mView.findViewById(R.id.profileViewPager)

        profilePagerAdapter = ProfilePagerAdapter((context as MenuPrincipalActivity).supportFragmentManager)

        profilePagerAdapter.addFragment(PerfilPersonalFragment(), "Dades personals")
        profilePagerAdapter.addFragment(PerfilMedicFragment(), "Dades mèdiques")

        viewPager.adapter = profilePagerAdapter

        tabHost.setupWithViewPager(viewPager)
    }

    private fun viewPagerListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }
            override fun onPageScrolled(position: Int, p1: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(p0: Int) {
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}