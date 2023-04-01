package com.davidnasrulloh.simplegithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.simplegithubuser.data.local.FavoriteUserRepository
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import com.davidnasrulloh.simplegithubuser.data.network.api.ApiConfig
import com.davidnasrulloh.simplegithubuser.data.network.response.User
import com.davidnasrulloh.simplegithubuser.utils.Utils.Companion.TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.davidnasrulloh.simplegithubuser.data.local.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: FavoriteUserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _callCounter = MutableLiveData(0)
    val callCounter: LiveData<Int> = _callCounter

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user


    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }


    fun getUserDetail(username: String) {
        _isLoading.value = true
        _callCounter.value = 1

        ApiConfig.getApiService().getUserDetail(token = "Bearer $TOKEN", username).apply {
            enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) _user.value = response.body()
                    else Log.e(TAG, response.message())

                    _isLoading.value = false
                    _isError.value = false
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, t.message.toString())

                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }
    }

    fun saveAsFavorite(user: FavoriteEntity) {
        viewModelScope.launch {
            repository.saveUserAsFavorite(user)
        }
    }

    fun deleteFromFavorite(user: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFromFavorite(user)
        }
    }

    fun isFavoriteUser(id: String): Flow<Boolean> = repository.isFavoriteUser(id)

}