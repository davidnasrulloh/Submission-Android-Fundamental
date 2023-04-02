package com.davidnasrulloh.simplegithubuser.ui.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidnasrulloh.simplegithubuser.data.local.FavoriteUserRepository
import com.davidnasrulloh.simplegithubuser.data.local.preferences.DarkModePreferences
import com.davidnasrulloh.simplegithubuser.di.Injection
import com.davidnasrulloh.simplegithubuser.ui.detail.DetailViewModel
import com.davidnasrulloh.simplegithubuser.ui.detail.followers.FollowersViewModel
import com.davidnasrulloh.simplegithubuser.ui.detail.following.FollowingViewModel
import com.davidnasrulloh.simplegithubuser.ui.favorite.FavoriteViewModel
import com.davidnasrulloh.simplegithubuser.ui.main.MainViewModel
import com.davidnasrulloh.simplegithubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val favoriteUserRepository: FavoriteUserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(favoriteUserRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(favoriteUserRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

    }


    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                ViewModelFactory(Injection.provideRepository(context)).also {
                    INSTANCE = it
                }
            }
        }
    }
}