package com.abrebo.countryquiz.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.GameCategory
import com.abrebo.countryquiz.databinding.AdItemBinding
import com.abrebo.countryquiz.databinding.GameCategoryItemBinding
import com.abrebo.countryquiz.ui.fragment.HomeFragmentDirections
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class GameCategoryAdapter(
    val context: Context,
    private val categoryList: List<GameCategory>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var adView: AdView
    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_AD = 1
        private const val AD_POSITION = 4
    }

    inner class CategoryViewHolder(val binding: GameCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
    inner class AdViewHolder(val binding: AdItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CATEGORY) {
            val binding = GameCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
            CategoryViewHolder(binding)
        } else {
            val binding = AdItemBinding.inflate(LayoutInflater.from(context), parent, false)
            AdViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == AD_POSITION) VIEW_TYPE_AD else VIEW_TYPE_CATEGORY
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val categoryPosition = if (position < AD_POSITION) position else position - 1
            val category = categoryList[categoryPosition]
            val binding = holder.binding
            binding.kategoryTitle.text = category.title
            binding.kategoryDescription.text = category.description
            binding.rankButton.paintFlags =binding.rankButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            binding.rankText.text=context.getString(R.string.siralama) +": "+category.rank.toString()

            binding.startGameButton.setOnClickListener {
                val navDirection = HomeFragmentDirections.actionHomeFragmentToGameFragment(category.id)
                Navigation.findNavController(it).navigate(navDirection)
            }
            binding.rankButton.setOnClickListener {
                val navDirection = HomeFragmentDirections.actionHomeFragmentToRankFragment(category.id)
                Navigation.findNavController(it).navigate(navDirection)
            }
            binding.highestScoreText.text = context.getString(R.string.en_yuksek_skorum) +" "+ category.highestScore

        } else if (holder is AdViewHolder) {
            val binding=holder.binding
            MobileAds.initialize(context) {}

            // Setup Banner Ad
            adView = AdView(context)
            adView.adUnitId = "ca-app-pub-4667560937795938/2952706283"
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
            binding.adView.removeAllViews()
            binding.adView.addView(adView)

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }
}
