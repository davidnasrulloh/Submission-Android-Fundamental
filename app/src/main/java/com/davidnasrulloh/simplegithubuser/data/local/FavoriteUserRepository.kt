package com.davidnasrulloh.simplegithubuser.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.davidnasrulloh.simplegithubuser.BuildConfig
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import com.davidnasrulloh.simplegithubuser.data.local.room.FavoriteUserDao
import com.davidnasrulloh.simplegithubuser.data.network.api.ApiService
import com.davidnasrulloh.simplegithubuser.data.network.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.davidnasrulloh.simplegithubuser.data.local.Result

class FavoriteUserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUerDao: FavoriteUserDao
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    companion object {
        private val TAG = FavoriteUserRepository::class.java
        private const val API_TOKEN = "Bearer ${BuildConfig.API_KEY}"
        private var INSTANCE: FavoriteUserRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteUerDao: FavoriteUserDao
        ): FavoriteUserRepository {
            return INSTANCE ?: synchronized(this) {
                FavoriteUserRepository(apiService, favoriteUerDao).also {
                    INSTANCE = it
                }
            }
        }
    }


    fun isFavoriteUser(id: String): Flow<Boolean> = favoriteUerDao.isFavoriteUser(id)
    fun getAllFavoriteUsers(): Flow<List<FavoriteEntity>> = favoriteUerDao.getAllUsers()

    suspend fun deleteFromFavorite(user: FavoriteEntity) {
        favoriteUerDao.delete(user)
    }

    suspend fun saveUserAsFavorite(user: FavoriteEntity) {
        favoriteUerDao.insert(user)
    }
//    fun getBookmarkedFavorite(): LiveData<List<FavoriteEntity>> {
//        return favoriteUerDao.getUserFavorite()
//    }
//
//    suspend fun setFavoriteBookmark(user: FavoriteEntity, bookmarkState: Boolean) {
//        user.isFavorite = bookmarkState
//        favoriteUerDao.update(user)
//    }


//    fun isFavoriteUser(id: String): Flow<Boolean> = favoriteUerDao.isFavoriteUser(id)
//
//    fun getAllFavoriteUsers(): Flow<List<FavoriteEntity>> = favoriteUerDao.getAllFavorites()
//
//    fun deleteFromFavorite(user: FavoriteEntity) {
//        executorService.execute { favoriteUerDao.delete(user) }
//    }
//
//    fun saveUserAsFavorite(user: FavoriteEntity) {
//        executorService.execute { favoriteUerDao.insert(user) }
//    }
}