package com.davidnasrulloh.simplegithubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.davidnasrulloh.simplegithubuser.data.local.FavoriteUserRepository
import com.davidnasrulloh.simplegithubuser.data.local.room.FavoriteUserDatabase
import com.davidnasrulloh.simplegithubuser.data.network.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()

        return FavoriteUserRepository.getInstance(apiService, dao)
    }
}