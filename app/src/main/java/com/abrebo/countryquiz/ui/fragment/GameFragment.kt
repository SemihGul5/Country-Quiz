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
        val id=GameFragmentArgs.fromBundle(requireArguments()).id
        if (id==1){
            setupProgressAndTimer(10,10,10000)
        }else if(id==6){
            setupProgressAndTimer(60,60,60000)
        }

        setupObservers()
        setupClickListeners(id)
        startTimer(id)

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
    private fun setupProgressAndTimer(progress:Int,progressMax:Int,timeLeftInMilis:Long){
        binding.progressBar.progress=progress
        binding.progressBar.max=progressMax
        timeLeftInMillis=timeLeftInMilis
    }
    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { flagQuestion ->
            binding.countryNameText.text = flagQuestion.correctAnswer
            imageViews.forEachIndexed { index, imageView ->
                imageView.setImageResource(flagQuestion.options[index] as Int)
                imageView.tag = flagQuestion.options[index]
            }
        }
        viewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreText.text = "Skor: $score"
        }
    }

    private fun setupClickListeners(id: Int) {
        imageViews.forEach { imageView ->
            imageView.setOnClickListener { view ->
                val selectedDrawable = view.tag as Int
                if (!viewModel.checkAnswer(selectedDrawable)) {
                    showScoreDialog()
                } else {
                    viewModel.nextQuestion()
                    if (id == 1) {
                        resetTimer(10000,id)
                    }
                }
            }
        }
    }

    private fun startTimer(id:Int) {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer(id)
            }

            override fun onFinish() {
                showScoreDialog()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimer(id: Int) {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text = "Süre: $secondsLeft"
        binding.progressBar.progress = secondsLeft

        when (id) {
            1 -> {
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
            6 -> {
                when {
                    secondsLeft > 40 -> {
                        binding.timerText.setTextColor(resources.getColor(R.color.green))
                        binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
                    }
                    secondsLeft > 20 -> {
                        binding.timerText.setTextColor(resources.getColor(R.color.yellow))
                        binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
                    }
                    else -> {
                        binding.timerText.setTextColor(resources.getColor(R.color.red))
                        binding.progressBar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
                    }
                }
            }
        }
    }

    private fun resetTimer(newTimeInMillis: Long,id:Int) {
        countDownTimer.cancel()
        timeLeftInMillis = newTimeInMillis
        startTimer(id)
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

