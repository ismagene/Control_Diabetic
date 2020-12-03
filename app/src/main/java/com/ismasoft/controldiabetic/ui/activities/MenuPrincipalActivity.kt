package com.ismasoft.controldiabetic.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.databinding.ActivityMenuPrincipalBinding
import com.ismasoft.controldiabetic.ui.fragments.*
import com.ismasoft.controldiabetic.viewModel.MenuPrincipalViewModel

class MenuPrincipalActivity : AppCompatActivity() {

    private val mFragmentManager = supportFragmentManager

    private lateinit var viewModel: MenuPrincipalViewModel
    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initNav()

    }

    private fun initNav() {

        var activeFragment : Fragment

        var alarmesFragment = AlarmesFragment()
        var controlsFragment = ControlsFragment()
        var perfilPersonalFragment = PerfilPersonalFragment()
        var visitesFragment = VisitesFragment()
        var perfilMedicFragment = PerfilMedicFragment()
        activeFragment = controlsFragment

        mFragmentManager.beginTransaction().add(R.id.frameLayout, alarmesFragment, "alarmes").hide(alarmesFragment).commit()
        mFragmentManager.beginTransaction().add(R.id.frameLayout, visitesFragment, "visites").hide(visitesFragment).commit()
        mFragmentManager.beginTransaction().add(R.id.frameLayout, perfilPersonalFragment, "perfilPersonal").hide(perfilPersonalFragment).commit()
        mFragmentManager.beginTransaction().add(R.id.frameLayout, controlsFragment, "controls").commit()


        this.binding.navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.controls -> {
                    mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_scale_in, R.anim.fade_scale_out).hide(activeFragment).show(controlsFragment).commit()
                    activeFragment = controlsFragment

                }
                R.id.alarmes -> {
                    mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_scale_in, R.anim.fade_scale_out).hide(activeFragment).show(alarmesFragment).commit()
                    activeFragment = alarmesFragment
                }
                R.id.visites-> {
                    mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_scale_in, R.anim.fade_scale_out).hide(activeFragment).show(visitesFragment).commit()
                    activeFragment = visitesFragment

                }
                R.id.perfil -> {
                    mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_scale_in, R.anim.fade_scale_out).hide(activeFragment).show(perfilPersonalFragment).commit()
                    activeFragment = perfilPersonalFragment
                }
            }
            true
        }

        this.binding.navigation.setOnNavigationItemReselectedListener {
            Log.d("MainActivity", "Fragment reselected")
        }
    }

}