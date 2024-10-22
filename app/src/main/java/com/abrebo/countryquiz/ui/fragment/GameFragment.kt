package com.abrebo.countryquiz.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentGameBinding
import com.abrebo.countryquiz.ui.viewmodel.FlagQuizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: FlagQuizViewModel by viewModels()
    private lateinit var imageViews: List<ImageView>
    private var timeLeftInMillis: Long = 60000
    private lateinit var countDownTimer: CountDownTimer
    private var isGameFinished = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)

        setupObservers()
        setupClickListeners()
        startTimer()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showScoreDialog()
            }
        })

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            if (!isGameFinished) {
                showScoreDialog()
            }
            true
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isGameFinished) {
            showScoreDialog()
        }
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
                if (!viewModel.checkAnswer(selectedDrawable)) {
                    showScoreDialog()
                } else {
                    viewModel.nextQuestion()
                }
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                showScoreDialog()
            }
        }.start()
    }

    private fun updateTimer() {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text = "Süre: $secondsLeft"
        binding.progressBar.progress = secondsLeft
        when {
            secondsLeft > 20 -> {
                binding.timerText.setTextColor(resources.getColor(R.color.green))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
            }
            secondsLeft > 10 -> {
                binding.timerText.setTextColor(resources.getColor(R.color.yellow))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
            }
            else -> {
                binding.timerText.setTextColor(resources.getColor(R.color.red))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
            }
        }
    }

    private fun showScoreDialog() {
        if (isGameFinished) return
        countDownTimer.cancel()
        isGameFinished = true
        val score = viewModel.score.value ?: 0
        val auth=FirebaseAuth.getInstance()
        val email=auth.currentUser?.email!!
        viewModel.getUserNameByEmail(email){
            if (it!=null){
                viewModel.updateScore(score, it)
            }
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Oyun Bitti!")
            .setMessage("Skorunuz: $score")
            .setPositiveButton("Tamam") { _, _ ->
                Navigation.findNavController(binding.root).navigate(R.id.action_gameFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }
}

