package com.abrebo.countryquiz.ui.adapter
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abrebo.countryquiz.R
import com.abrebo.countryquiz.data.model.RankUser
import com.abrebo.countryquiz.databinding.RankItemBinding
import com.abrebo.countryquiz.ui.viewmodel.UserViewModel

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
            binding.rankText.text = "Rank"
            binding.userNameText.text = "User Name"
            binding.scoreText.text = "Score"
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
        setupbackground(position,binding)

    }

    private fun setUserBackground(user: RankUser,binding: RankItemBinding) {
        if (user.userName == userName) {
            binding.rankText.setTypeface(null, Typeface.BOLD)
            binding.userNameText.setTypeface(null, Typeface.BOLD)
            binding.scoreText.setTypeface(null, Typeface.BOLD)
        }
    }


    private fun setupbackground(position: Int,binding: RankItemBinding) {
        if (position%2==0){
            binding.linearLayoutDashboardItem.setBackgroundColor(context.getColor(R.color.grey))
        }else{
            binding.linearLayoutDashboardItem.setBackgroundColor(context.getColor(R.color.white))
        }
    }
}
