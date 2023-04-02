package com.davidnasrulloh.simplegithubuser.ui.setting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.davidnasrulloh.simplegithubuser.data.local.preferences.DarkModePreferences
import kotlinx.coroutines.launch


class SettingViewModel(private val preferences: DarkModePreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
}