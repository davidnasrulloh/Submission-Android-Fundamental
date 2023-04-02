package com.davidnasrulloh.simplegithubuser.ui.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.simplegithubuser.data.local.preferences.DarkModePreferences
import kotlinx.coroutines.launch

class SplashViewModel(private val preferences: DarkModePreferences) : ViewModel() {
    fun isDarkMode(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun changeTheme(isDarkModeAcitve: Boolean){
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeAcitve)
        }
    }
}