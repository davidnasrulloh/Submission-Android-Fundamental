package com.davidnasrulloh.simplegithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.simplegithubuser.data.local.FavoriteUserRepository
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import com.davidnasrulloh.simplegithubuser.data.network.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.davidnasrulloh.simplegithubuser.data.local.Result

class DetailViewModel(private val repository: FavoriteUserRepository) : ViewModel() {

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    private val _userDetail = MutableStateFlow<Result<User>>(Result.Loading)
    val userDetail = _userDetail.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }


    fun getDetailUser(username: String) {
        _userDetail.value = Result.Loading
        viewModelScope.launch {
            repository.getUserDetail(username).collect {
                _userDetail.value = it
            }
        }

        _isLoaded.value = true
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