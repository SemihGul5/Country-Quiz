package com.abrebo.countryquiz.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentGameBinding
import com.abrebo.countryquiz.ui.viewmodel.FlagQuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: FlagQuizViewModel by viewModels()
    private lateinit var imageViews: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)

        setupObservers()
        setupClickListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { flagQuestion ->
            binding.countryNameText.text = flagQuestion.correctAnswer
            imageViews.forEachIndexed { index, imageView ->
                imageView.setImageResource(flagQuestion.options[index])
                imageView.tag = flagQuestion.options[index]
            }
        }
        viewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreText.text = "Skor: $score"
        }
    }
    private fun setupClickListeners() {
        imageViews.forEach { imageView ->
            imageView.setOnClickListener { view ->
                val selectedDrawable = view.tag as Int
                viewModel.checkAnswer(selectedDrawable)
            }
        }
    }
}
