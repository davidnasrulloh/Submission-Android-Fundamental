package com.davidnasrulloh.simplegithubuser.ui.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidnasrulloh.simplegithubuser.R
import com.davidnasrulloh.simplegithubuser.adapter.ListUserAdapter
import com.davidnasrulloh.simplegithubuser.data.network.response.SimpleUser
import com.davidnasrulloh.simplegithubuser.databinding.ActivityMainBinding
import com.davidnasrulloh.simplegithubuser.ui.view.DetailUserActivity.Companion.EXTRA_DETAIL
import com.davidnasrulloh.simplegithubuser.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainViewModel.simpleUsers.observe(this) {
            showSearchingResult(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) { error ->
            if (error) errorOccurred()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.github_username)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.findUser(query ?: "")
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        return true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun errorOccurred() {
        Toast.makeText(this@MainActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }


    private fun showSearchingResult(user: ArrayList<SimpleUser>) {
        binding.tvResultCount.text = getString(R.string.showing_results, user.size)

        val listUserAdapter = ListUserAdapter(user)

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listUserAdapter
            setHasFixedSize(true)
        }

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: SimpleUser) {
                goToDetailUser(user)
            }

        })
    }


    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@MainActivity, DetailUserActivity::class.java).apply {
            putExtra(EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}