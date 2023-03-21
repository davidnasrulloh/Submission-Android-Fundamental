package com.davidnasrulloh.simplegithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidnasrulloh.simplegithubuser.data.network.response.SimpleUser
import com.davidnasrulloh.simplegithubuser.databinding.UserCardBinding
import com.davidnasrulloh.simplegithubuser.utils.Utils.Companion.setImageGlide

class ListUserAdapter(private val listUser: ArrayList<SimpleUser>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            cardTvUsername.text = user.login
            cardImageProfile.setImageGlide(holder.itemView.context, user.avatarUrl)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(user: SimpleUser)
    }
}