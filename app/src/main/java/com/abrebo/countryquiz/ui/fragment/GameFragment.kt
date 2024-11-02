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
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.databinding.FragmentGameBinding
import com.abrebo.countryquiz.ui.viewmodel.FlagQuizViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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
    private lateinit var answerButtons: List<Button>
    private var id: Int = 0
    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id=GameFragmentArgs.fromBundle(requireArguments()).id
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/6106488823"
        adView.setAdSize(AdSize.LARGE_BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        viewModel.loadInterstitialAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.nextQuestion(id)

        when (id) {
            1 -> {
                binding.materialToolbar2.title=requireContext().getString(R.string.FindtheFlagbyCountryName)
                imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)
                viewModel.prepareQuestionsGame1()
                setupProgressAndTimer(5,5,5000)
            }
            6 -> {
                binding.materialToolbar2.title=requireContext().getString(R.string.FindtheFlagbyCountryName)
                imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)
                viewModel.prepareQuestionsGame1()
                setupProgressAndTimer(60,60,60000)
            }
            2 -> {
                binding.materialToolbar2.title=requireContext().getString(R.string.Findthecountrybytheflag)
                viewModel.prepareQuestionsGame2()
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupProgressAndTimer(5,5,5000)
                binding.game1CountryNameText.visibility=View.GONE
                binding.game1LinearLayout.visibility=View.GONE
                binding.game2FlagImage.visibility=View.VISIBLE
                binding.game2LinearLayout.visibility=View.VISIBLE
            }
            3->{
                binding.materialToolbar2.title=requireContext().getString(R.string.FindtheCapitaloftheCountry)
                viewModel.prepareQuestionsGame3Capital()
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupProgressAndTimer(10,10,10000)
                binding.game1LinearLayout.visibility=View.GONE
                binding.game2LinearLayout.visibility=View.VISIBLE
            }
            4->{
                binding.materialToolbar2.title=requireContext().getString(R.string.FindthePopulationoftheCountry)
                viewModel.prepareQuestionsGame4Population()
                answerButtons = listOf(binding.answer1, binding.answer2)
                setupProgressAndTimer(10,10,10000)
                binding.game1LinearLayout.visibility=View.GONE
                binding.game2LinearLayout.visibility=View.VISIBLE
                binding.answer3.visibility=View.GONE
                binding.answer4.visibility=View.GONE
            }
            7->{
                binding.materialToolbar2.title=requireContext().getString(R.string.FindtheCountrybyGeographicLocation)
                viewModel.prepareQuestionsGame7Map()
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupProgressAndTimer(10,10,10000)
                binding.game1CountryNameText.visibility=View.GONE
                binding.game1LinearLayout.visibility=View.GONE
                binding.game2FlagImage.visibility=View.VISIBLE
                binding.game2LinearLayout.visibility=View.VISIBLE
            }
            8->{
                binding.materialToolbar2.title=requireContext().getString(R.string.FindtheContinentoftheCountry)
                viewModel.prepareQuestionsGame8Continent()
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupProgressAndTimer(5,5,5000)
                binding.game1LinearLayout.visibility=View.GONE
                binding.game2LinearLayout.visibility=View.VISIBLE
            }
        }

        setupObservers(id)
        setupClickListeners(id)
        startTimer(id)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showScoreDialog(id)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if (!isGameFinished) {
            countDownTimer.cancel()
            showScoreDialog(id)
        }
    }
    private fun setupProgressAndTimer(progress:Int,progressMax:Int,timeLeftInMilis:Long){
        binding.progressBar.progress=progress
        binding.progressBar.max=progressMax
        timeLeftInMillis=timeLeftInMilis
    }
    @SuppressLint("SetTextI18n")
    private fun setupObservers(id:Int) {
        viewModel.currentQuestion.observe(viewLifecycleOwner){flagQuestion->
            when (id) {
                1, 6 -> {
                    binding.game1CountryNameText.text = flagQuestion.countryName
                    imageViews.forEachIndexed { index, imageView ->
                        imageView.setImageResource(flagQuestion.options[index] as Int)
                        imageView.tag = flagQuestion.options[index]
                    }
                }
                2 -> {
                    binding.game2FlagImage.setImageResource(flagQuestion.flagDrawable)
                    answerButtons.forEachIndexed { index, button ->
                        button.text = flagQuestion.options[index].toString()
                        button.tag = flagQuestion.options[index]
                    }
                }
                7 -> {
                    binding.game2FlagImage.setImageResource(flagQuestion.mapDrawable)
                    answerButtons.forEachIndexed { index, button ->
                        button.text = flagQuestion.options[index].toString()
                        button.tag = flagQuestion.options[index]
                    }
                }
                3,4,8 -> {
                    binding.game1CountryNameText.text = flagQuestion.countryName
                    answerButtons.forEachIndexed { index, button ->
                        button.text = flagQuestion.options[index].toString()
                        button.tag = flagQuestion.options[index]
                    }
                }
            }
        }

        viewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreText.text = "Skor: $score"
        }
    }

    private fun setupClickListeners(id: Int) {
        when (id) {
            1, 6 -> {
                imageViews.forEach { imageView ->
                    imageView.setOnClickListener { view ->
                        val selectedDrawable = view.tag as Int
                        if (!viewModel.checkAnswer(selectedDrawable)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            if (id == 1) {
                                resetTimer(5000,id)
                            }
                        }
                    }
                }
            }
            2 -> {
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selectedCountryName = view.tag as String
                        if (!viewModel.checkAnswer(selectedCountryName)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(5000,id)
                        }
                    }
                }
            }
            7 -> {
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selectedCountryName = view.tag as String
                        if (!viewModel.checkAnswer(selectedCountryName)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(10000,id)
                        }
                    }
                }
            }
            3->{
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selectedPopulation = view.tag as String
                        if (!viewModel.checkAnswerGame3Capital(selectedPopulation)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(10000,id)
                        }
                    }
                }
            }
            4 -> {
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selectedPopulation = view.tag as String
                        if (!viewModel.checkAnswerGame4Population(selectedPopulation)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(10000,id)
                        }
                    }
                }
            }
            8->{
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selectedPopulation = view.tag as String
                        if (!viewModel.checkAnswerGame8Continent(selectedPopulation)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(5000,id)
                        }
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
                showScoreDialog(id)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimer(id: Int) {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text =requireContext().getString(R.string.time)+secondsLeft
        binding.progressBar.progress = secondsLeft

        when (id) {
            6 -> {
                when {
                    secondsLeft > 40 -> {
                        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                        binding.timerText.setTextColor(greenColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
                    }
                    secondsLeft > 20 -> {
                        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        binding.timerText.setTextColor(yellowColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(yellowColor)
                    }
                    else -> {
                        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.timerText.setTextColor(redColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(redColor)
                    }
                }
            }
            1, 2,8 -> {
                when {
                    secondsLeft > 4 -> {
                        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                        binding.timerText.setTextColor(greenColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
                    }
                    secondsLeft > 2 -> {
                        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        binding.timerText.setTextColor(yellowColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(yellowColor)
                    }
                    else -> {
                        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.timerText.setTextColor(redColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(redColor)
                    }
                }
            }
            else -> {
                when {
                    secondsLeft > 7 -> {
                        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                        binding.timerText.setTextColor(greenColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
                    }
                    secondsLeft > 3 -> {
                        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        binding.timerText.setTextColor(yellowColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(yellowColor)
                    }
                    else -> {
                        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.timerText.setTextColor(redColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(redColor)
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
    private fun showScoreDialog(id:Int) {
        if (isGameFinished) return
        countDownTimer.cancel()
        isGameFinished = true
        val score = viewModel.score.value ?: 0

        viewModel.getUserName(){
            if (it!=null){
                viewModel.updateScore(score,it.toString(),id)
            }
        }
        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.oyun_bitti))
            .setMessage(requireContext().getString(R.string.skorunuz)+score)
            .setPositiveButton(requireContext().getString(R.string.tamam)) { _, _ ->
                viewModel.showInterstitialAd(requireActivity())
                Navigation.findNavController(binding.root).navigate(R.id.action_gameFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }
}

