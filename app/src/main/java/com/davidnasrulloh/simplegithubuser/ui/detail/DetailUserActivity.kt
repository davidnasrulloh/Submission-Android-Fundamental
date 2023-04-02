package com.davidnasrulloh.simplegithubuser.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.davidnasrulloh.simplegithubuser.R
import com.davidnasrulloh.simplegithubuser.adapter.SectionPagerAdapter
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import com.davidnasrulloh.simplegithubuser.data.network.response.User
import com.davidnasrulloh.simplegithubuser.databinding.ActivityDetailUserBinding
import com.davidnasrulloh.simplegithubuser.ui.view.ViewModelFactory
import com.davidnasrulloh.simplegithubuser.utils.EspressoIdlingResource
import com.davidnasrulloh.simplegithubuser.utils.Utils.Companion.setAndVisible
import com.davidnasrulloh.simplegithubuser.utils.Utils.Companion.setImageGlide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username: String? = null
    private var profileUrl: String? = null
    private var userDetail: FavoriteEntity? = null
    private var isFavorite: Boolean? = false

    //    private lateinit var detailViewModel: DetailViewModel
    private val detailViewModel: DetailViewModel by viewModels() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        username = intent.extras?.get(EXTRA_DETAIL) as String

        setContentView(binding.root)
        setViewPager()
        setToolbar(getString(R.string.profile))

//        detailViewModel = obtainViewModel(this)

        detailViewModel.user.observe(this) { user ->
            if (user != null) {
                parseUserDetail(user)
                profileUrl = user.htmlUrl
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isError.observe(this) { error ->
            if (error) errorOccurred()
        }

        detailViewModel.callCounter.observe(this) { counter ->
            if (counter < 1) detailViewModel.getUserDetail(username!!)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.isFavoriteUser(username ?: "").collect { state ->
                        isFavoriteUser(state)
                        isFavorite = state
                    }
                }
            }
        }


        binding.btnOpen.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)
    }

    private fun setLoading(state: Boolean) {
        binding.pbLoading.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        EspressoIdlingResource.increment()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_open -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(profileUrl)
                }.also {
                    startActivity(it)
                }
            }
            R.id.fab_favorite -> {
                if (isFavorite == true) {
                    userDetail?.let { detailViewModel.deleteFromFavorite(it) }
                    isFavoriteUser(false)
                    Toast.makeText(this, "User deleted from favorite", Toast.LENGTH_SHORT).show()
                } else {
                    userDetail?.let { detailViewModel.saveAsFavorite(it) }
                    isFavoriteUser(true)
                    Toast.makeText(this, "User added to favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        username = null
        profileUrl = null
        isFavorite = null
        super.onDestroy()
    }

    private fun isFavoriteUser(favorite: Boolean) {
        if (favorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun errorOccurred() {
        binding.apply {
            userDetailContainer.visibility = View.INVISIBLE
            tabs.visibility = View.INVISIBLE
            viewPager.visibility = View.INVISIBLE
        }
        Toast.makeText(this@DetailUserActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbarDetail)
        binding.collapsingToolbar.isTitleEnabled = false
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }


    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionPagerAdapter(this, username!!)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                pbLoading.visibility = View.VISIBLE
                appBarLayout.visibility = View.INVISIBLE
                viewPager.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                pbLoading.visibility = View.GONE
                appBarLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }
        }
    }


    private fun parseUserDetail(user: User) {
        binding.apply {
            tvUsername.text = user.login
            tvRepositories.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()

            tvName.setAndVisible(user.name)
            tvBio.setAndVisible(user.bio)
            tvCompany.setAndVisible(user.company)
            tvLocation.setAndVisible(user.location)
            tvBlog.setAndVisible(user.blog)

            ivAvatar.setImageGlide(this@DetailUserActivity, user.avatarUrl)
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}