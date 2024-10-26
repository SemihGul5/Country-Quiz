package com.abrebo.countryquiz.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.GameCategory
import com.abrebo.countryquiz.databinding.GameCategoryItemBinding
import com.abrebo.countryquiz.ui.fragment.HomeFragmentDirections

class GameCategoryAdapter(
    val context: Context,
    private val categoryList: List<GameCategory>
) : RecyclerView.Adapter<GameCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: GameCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = GameCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        val binding = holder.binding
        binding.kategoryTitle.text = category.title
        binding.kategoryDescription.text = category.description

        binding.startGameButton.setOnClickListener {
            when (category.id) {
                4 -> {
                    Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_populationQuizFragment)
                }
                1, 6 -> {
                    val navDirection=HomeFragmentDirections.actionHomeFragmentToGameFragment(category.id)
                    Navigation.findNavController(it).navigate(navDirection)
                }
                2 -> {
                    val navDirection=HomeFragmentDirections.actionHomeFragmentToGameFragment(category.id)
                    Navigation.findNavController(it).navigate(navDirection)
                }


            }
        }


    }
}
