package com.abrebo.countryquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.abrebo.countryquiz.databinding.ActivityMainPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}