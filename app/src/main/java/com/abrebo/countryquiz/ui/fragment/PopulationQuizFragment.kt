package com.abrebo.countryquiz.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentPopulationQuizBinding
import com.abrebo.countryquiz.ui.viewmodel.PopulationQuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopulationQuizFragment : Fragment() {
    private lateinit var binding: FragmentPopulationQuizBinding
    private val viewModel: PopulationQuizViewModel by viewModels()
    private var timeLeftInMillis: Long = 10000
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var answerButtons: List<Button>
    private var isGameFinished = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPopulationQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)

        setupObservers()
        setupClickListeners()
        startTimer()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showScoreDialog()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        if (!isGameFinished) {
            showScoreDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { populationQuestion ->
            binding.countryNameText.text = populationQuestion.countryName
            answerButtons.forEachIndexed { index, button ->
                button.text = populationQuestion.options[index]
                button.tag = populationQuestion.options[index]
            }
        }

        viewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreText.text = "Skor: $score"
        }
    }

    private fun setupClickListeners() {
        answerButtons.forEach { button ->
            button.setOnClickListener { view ->
                val selectedPopulation = view.tag as String
                if (!viewModel.checkAnswer(selectedPopulation)) {
                    showScoreDialog()
                } else {
                    viewModel.nextQuestion()
                    resetTimer(10000)
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

    @SuppressLint("SetTextI18n")
    private fun updateTimer() {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text = "Süre: $secondsLeft"
        binding.progressBar.progress = secondsLeft

        when {
            secondsLeft > 7 -> {
                binding.timerText.setTextColor(resources.getColor(R.color.green))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
            }
            secondsLeft > 3 -> {
                binding.timerText.setTextColor(resources.getColor(R.color.yellow))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
            }
            else -> {
                binding.timerText.setTextColor(resources.getColor(R.color.red))
                binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
            }
        }
    }

    private fun resetTimer(newTimeInMillis: Long) {
        countDownTimer.cancel()
        timeLeftInMillis = newTimeInMillis
        startTimer()
    }

    private fun showScoreDialog() {
        if (isGameFinished) return
        countDownTimer.cancel()
        isGameFinished = true
        val score = viewModel.score.value ?: 0
        AlertDialog.Builder(requireContext())
            .setTitle("Oyun Bitti!")
            .setMessage("Skorunuz: $score")
            .setPositiveButton("Tamam") { _, _ ->
                Navigation.findNavController(binding.root).navigate(R.id.action_populationQuizFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }
}
