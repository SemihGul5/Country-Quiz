package com.abrebo.countryquiz.ui.adapter
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.RankUser
import com.abrebo.countryquiz.databinding.RankItemBinding

class RankAdapter(
    val context: Context,
    private val userList: List<RankUser>,
    private val userName:String
) : RecyclerView.Adapter<RankAdapter.RankHolder>() {

    inner class RankHolder(val binding: RankItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val binding = RankItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RankHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size + 1
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        val binding = holder.binding
        if (position == 0) {
            binding.rankText.text = context.getString(R.string.siralama)
            binding.userNameText.text = context.getString(R.string.kullanici_adi)
            binding.scoreText.text = context.getString(R.string.skor)
            binding.rankText.setTypeface(null, Typeface.BOLD)
            binding.userNameText.setTypeface(null, Typeface.BOLD)
            binding.scoreText.setTypeface(null, Typeface.BOLD)
        } else {
            val user = userList[position - 1]
            binding.rankText.text = user.rank.toString()
            binding.userNameText.text = user.userName
            binding.scoreText.text = user.highestScore.toString()
            binding.rankText.setTypeface(null, Typeface.NORMAL)
            binding.userNameText.setTypeface(null, Typeface.NORMAL)
            binding.scoreText.setTypeface(null, Typeface.NORMAL)
            setUserBackground(user,binding)
        }
        setupBackground(position,binding)

    }

    private fun setUserBackground(user: RankUser,binding: RankItemBinding) {
        if (user.userName == userName) {
            binding.rankText.setTypeface(null, Typeface.BOLD)
            binding.userNameText.setTypeface(null, Typeface.BOLD)
            binding.scoreText.setTypeface(null, Typeface.BOLD)
        }
    }

    private fun setupBackground(position: Int, binding: RankItemBinding) {
        val isDarkTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        val backgroundColor = if (isDarkTheme) {
            if (position % 2 == 0) R.color.black2 else R.color.backgroun_dark_game_category
        } else {
            if (position % 2 == 0) R.color.grey else R.color.white
        }
        val textColor = if (isDarkTheme) R.color.white else R.color.black
        binding.linearLayoutDashboardItem.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
        binding.rankText.setTextColor(ContextCompat.getColor(context, textColor))
        binding.userNameText.setTextColor(ContextCompat.getColor(context, textColor))
        binding.scoreText.setTextColor(ContextCompat.getColor(context, textColor))
    }
}
