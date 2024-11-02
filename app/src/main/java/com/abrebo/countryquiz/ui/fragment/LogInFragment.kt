package com.abrebo.countryquiz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.countryquiz.MainPageActivity
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentLogInBinding
import com.abrebo.countryquiz.ui.viewmodel.LogInViewModel
import com.abrebo.countryquiz.utils.BackPressUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var viewModel:LogInViewModel
    private lateinit var binding:FragmentLogInBinding
    private lateinit var userName:String
    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:LogInViewModel by viewModels()
        viewModel=temp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentLogInBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/2789054752"
        adView.setAdSize(AdSize.LARGE_BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackPressUtils.setBackPressCallback(this, viewLifecycleOwner)

        viewModel.userName.observe(viewLifecycleOwner){userName->
            if (userName!="No have a UserName"){
                val intent = Intent(requireContext(),MainPageActivity::class.java)
                startActivity(intent)
            }
        }

        binding.logInButton.setOnClickListener {
            logIn(it)
        }

    }

    private fun logIn(it:View) {
        userName = binding.userNameText.text.toString().lowercase()
        binding.progressBar.visibility = View.VISIBLE
        if (userName.isNotEmpty() && userName.length<20){
            viewModel.checkUserNameAvailability(userName){isAvaible->
                binding.progressBar.visibility = View.GONE
                if (isAvaible){
                    viewModel.saveUserName(userName)
                    Navigation.findNavController(it).navigate(R.id.action_logInFragment_to_mainPageActivity)
                }else{
                    Toast.makeText(requireContext(), requireContext().getString(R.string.usernamealreadyexists), Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(requireContext(),requireContext().getString(R.string.kullanici_adi_belirle),Toast.LENGTH_SHORT).show()
        }
    }
}