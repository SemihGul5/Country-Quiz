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
import com.abrebo.countryquiz.utils.BackPressUtils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankFragment : Fragment() {
    private lateinit var binding:FragmentRankBinding
    private val viewModel:UserViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        viewModel.getAllRankUsers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackPressUtils.setBackPressCallback(this, viewLifecycleOwner)
        viewModel.userRankList.observe(viewLifecycleOwner){
            viewModel.getUserNameByEmail(auth.currentUser?.email!!){userName->
                val adapter= RankAdapter(requireContext(),it,userName!!)
                binding.recyclerViewRank.adapter=adapter
            }
        }
    }

}