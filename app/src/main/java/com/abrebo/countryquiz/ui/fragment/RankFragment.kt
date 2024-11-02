package com.abrebo.countryquiz.ui.fragment

import com.abrebo.countryquiz.ui.adapter.RankAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.abrebo.countryquiz.databinding.FragmentRankBinding
import com.abrebo.countryquiz.ui.viewmodel.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankFragment : Fragment() {
    private lateinit var binding:FragmentRankBinding
    private val viewModel:UserViewModel by viewModels()

    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentRankBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/9583533554"
        adView.setAdSize(AdSize.BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id=RankFragmentArgs.fromBundle(requireArguments()).gameId
        binding.progressBar2.visibility = View.VISIBLE
        viewModel.getAllRankUsers(id)


        viewModel.userRankList.observe(viewLifecycleOwner){
            binding.progressBar2.visibility = View.GONE
            viewModel.getUserName(){userName->
                val adapter= RankAdapter(requireContext(),it,userName!!)
                binding.recyclerViewRank.adapter=adapter
            }
        }
    }
}