package com.abrebo.countryquiz.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.GameCategory
import com.abrebo.countryquiz.databinding.FragmentHomeBinding
import com.abrebo.countryquiz.ui.adapter.GameCategoryAdapter
import com.abrebo.countryquiz.ui.viewmodel.HomeViewModel
import com.abrebo.countryquiz.utils.BackPressUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val viewModel:HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.pacifico)
        binding.textView.typeface = typeface

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackPressUtils.setBackPressCallback(this, viewLifecycleOwner)

        viewModel.loadCategories()
        viewModel.categoryList.observe(viewLifecycleOwner){categoryList->
            val adapter=GameCategoryAdapter(requireContext(),categoryList)
            binding.recyclerViewGameCategory.adapter=adapter
        }


    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }
    fun initViewModel(){
        viewModel.getUserNameByEmail(auth.currentUser?.email!!){userName->
            if (userName!=null){
                viewModel.getHighestScore(userName,1)
                viewModel.getHighestScore(userName,2)
                viewModel.getHighestScore(userName,3)
                viewModel.getHighestScore(userName,4)
                viewModel.getHighestScore(userName,6)
                viewModel.getHighestScore(userName,7)
                viewModel.getHighestScore(userName,8)
            }
        }
    }
}