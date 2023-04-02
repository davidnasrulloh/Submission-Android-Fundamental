package com.davidnasrulloh.simplegithubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidnasrulloh.simplegithubuser.R
import com.davidnasrulloh.simplegithubuser.adapter.ListUserAdapter
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import com.davidnasrulloh.simplegithubuser.data.network.response.SimpleUser
import com.davidnasrulloh.simplegithubuser.databinding.ActivityFavoriteBinding
import com.davidnasrulloh.simplegithubuser.ui.detail.DetailUserActivity
import com.davidnasrulloh.simplegithubuser.ui.view.ViewModelFactory
import com.davidnasrulloh.simplegithubuser.utils.EspressoIdlingResource
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar(getString(R.string.favorite))

        lifecycleScope.launchWhenStarted {
            launch {
                favoriteViewModel.favorite.collect {
                    EspressoIdlingResource.increment()
                    if (it.isNotEmpty()) showFavoriteUsers(it)
                    else showMessage()
                }
            }
        }
    }

    private fun showFavoriteUsers(users: List<FavoriteEntity>) {
        val listUsers = ArrayList<SimpleUser>()

        users.forEach { user ->
            val data = SimpleUser(
                user.avatarUrl,
                user.id
            )

            listUsers.add(data)
        }

        val listUserAdapter = ListUserAdapter(listUsers)

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = listUserAdapter
            visibility = View.VISIBLE
            setHasFixedSize(true)
        }

        binding.tvMessage.visibility = View.GONE

        listUserAdapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }

        })

        EspressoIdlingResource.decrement()
    }

    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showMessage() {
        binding.tvMessage.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE

        EspressoIdlingResource.decrement()
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }


}