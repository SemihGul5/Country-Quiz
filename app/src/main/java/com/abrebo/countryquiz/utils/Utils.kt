package com.abrebo.countryquiz.utils

import android.os.SystemClock
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object BackPressUtils {

    private var lastBackPressedTime: Long = 0
    fun setBackPressCallback(fragment: Fragment, lifecycleOwner: LifecycleOwner) {
        val backButtonCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = SystemClock.elapsedRealtime()
                if (lastBackPressedTime + 2000 > currentTime) {
                    fragment.activity?.finishAffinity()
                } else {
                    Toast.makeText(fragment.context, "Çıkmak için tekrar basın", Toast.LENGTH_SHORT).show()
                }
                lastBackPressedTime = currentTime
            }
        }

        fragment.requireActivity().onBackPressedDispatcher.addCallback(lifecycleOwner, backButtonCallback)
    }
}
fun Fragment.setupBottomNavigation() {
    val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.homeFragment -> {
                Navigation.findNavController(requireView()).navigate(R.id.homeFragment)
                true
            }
            R.id.dashboardFragment -> {
                Navigation.findNavController(requireView()).navigate(R.id.dashboardFragment)
                true
            }
            R.id.profileFragment -> {
                Navigation.findNavController(requireView()).navigate(R.id.profileFragment)
                true
            }
            else -> false
        }
    }
}
