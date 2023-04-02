package com.davidnasrulloh.simplegithubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.simplegithubuser.data.local.FavoriteUserRepository
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteUserRepository) : ViewModel() {

    private val _favorites = MutableStateFlow(listOf<FavoriteEntity>())
    val favorite = _favorites.asStateFlow()

    init {
        getFavoriteUsers()
    }

    private fun getFavoriteUsers() {
        viewModelScope.launch {
            repository.getAllFavoriteUsers().collect {
                _favorites.value = it
            }
        }
    }

//    fun getBookmarkedFavorite() = repository.getBookmarkedFavorite()
//
//    fun saveFavorite(user: FavoriteEntity){
//        viewModelScope.launch {
//            repository.setFavoriteBookmark(user, true)
//        }
//    }
//
//    fun deleteFavorite(user: FavoriteEntity){
//        viewModelScope.launch {
//            repository.setFavoriteBookmark(user, false)
//        }
//    }
}