package com.abrebo.countryquiz.ui.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentHomeBinding
import com.abrebo.countryquiz.ui.adapter.GameCategoryAdapter
import com.abrebo.countryquiz.ui.viewmodel.HomeViewModel
import com.abrebo.countryquiz.utils.BackPressUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var adView: AdView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val UI_MODE = "ui_mode"
        const val UI_MODE_LIGHT = 1
        const val UI_MODE_DARK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("ThemePrefs", android.content.Context.MODE_PRIVATE)

        // Tema tercihini kontrol et ve uygula
        val uiMode = sharedPreferences.getInt(UI_MODE, UI_MODE_LIGHT)
        if (uiMode == UI_MODE_DARK) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.pacifico)
        binding.textView.typeface = typeface
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/5323718753"
        adView.setAdSize(AdSize.BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackPressUtils.setBackPressCallback(this, viewLifecycleOwner)

        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.profileFragmentToolbarMenu) {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_profileFragment2)
            }
            true
        }

        viewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            val adapter = GameCategoryAdapter(requireContext(), categoryList)
            binding.recyclerViewGameCategory.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getUserNameByEmail(auth.currentUser?.email!!) { userName ->
            if (userName != null) {
                viewModel.loadCategories(userName)
                viewModel.getHighestScore(userName, 1)
                viewModel.getHighestScore(userName, 2)
                viewModel.getHighestScore(userName, 3)
                viewModel.getHighestScore(userName, 4)
                viewModel.getHighestScore(userName, 6)
                viewModel.getHighestScore(userName, 7)
                viewModel.getHighestScore(userName, 8)
            }
        }
    }
}
