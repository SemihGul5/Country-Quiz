package com.abrebo.countryquiz.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.abrebo.countryquiz.MainActivity
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentProfileBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adView: AdView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val UI_MODE = "ui_mode"
        const val UI_MODE_LIGHT = 1
        const val UI_MODE_DARK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("ThemePrefs", android.content.Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/2166238228"
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsList = arrayListOf(
            requireContext().getString(R.string.sharetheApp),
            requireContext().getString(R.string.tema),
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, settingsList)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> shareApp()
                1 -> toggleTheme()

            }
        }
    }

    private fun toggleTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putInt(UI_MODE, UI_MODE_DARK).apply()
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putInt(UI_MODE, UI_MODE_LIGHT).apply()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }
    }


    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareMessage = "${requireContext().getString(R.string.share_app_text)} " +
                "https://play.google.com/store/apps/developer?id=Abrebo+Studio&pli=1"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, requireContext().getString(R.string.sharetheApp)))
    }
}
