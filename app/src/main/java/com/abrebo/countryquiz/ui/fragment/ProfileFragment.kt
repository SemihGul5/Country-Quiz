package com.abrebo.countryquiz.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.abrebo.countryquiz.MainActivity
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val settingsList=ArrayList<String>()
        settingsList.add("Uygulamayı Paylaş")
        settingsList.add("Çıkış Yap")

        val adapter= ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,settingsList)
        binding.listView.adapter=adapter

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            when(i){
                0->{shareApp()}
                1->{FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }

            }
        }
    }
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareMessage = "Bu harika uygulamayı denemenizi tavsiye ederim! Play Store bağlantısı: " +
                "https://play.google.com/store/apps/developer?id=Abrebo+Studio&pli=1"

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Uygulamayı Paylaş"))
    }

}