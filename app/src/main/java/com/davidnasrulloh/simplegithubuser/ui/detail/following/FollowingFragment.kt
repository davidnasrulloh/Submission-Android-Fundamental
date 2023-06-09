package com.davidnasrulloh.simplegithubuser.ui.detail.following

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidnasrulloh.simplegithubuser.adapter.ListUserAdapter
import com.davidnasrulloh.simplegithubuser.adapter.SectionPagerAdapter.Companion.ARGS_USERNAME
import com.davidnasrulloh.simplegithubuser.data.network.response.SimpleUser
import com.davidnasrulloh.simplegithubuser.databinding.FragmentFollowingBinding
import com.davidnasrulloh.simplegithubuser.ui.detail.DetailUserActivity

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)

        followingViewModel.following.observe(viewLifecycleOwner) { following ->
            if (following == null) {
                val username = arguments?.getString(ARGS_USERNAME) ?: ""
                followingViewModel.getUserFollowing(username)
            } else {
                showFollowing(following)
            }
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun showFollowing(users: ArrayList<SimpleUser>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = ListUserAdapter(users)

            binding.rvUsers.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: SimpleUser) {
                    goToDetailUser(user)
                }

            })
        } else binding.tvStatus.visibility = View.VISIBLE
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }


    private fun goToDetailUser(user: SimpleUser) {
        Intent(activity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}