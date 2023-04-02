package com.davidnasrulloh.simplegithubuser.data.local

import android.util.Log
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
import javax.inject.Inject

class FavoriteUserRepository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteUerDao: FavoriteUserDao
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    companion object {
        private val TAG = FavoriteUserRepository::class.java.simpleName
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


    fun getUserDetail(id: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
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
}