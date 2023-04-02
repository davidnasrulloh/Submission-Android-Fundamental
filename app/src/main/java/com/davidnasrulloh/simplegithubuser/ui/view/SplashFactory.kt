package com.davidnasrulloh.simplegithubuser.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davidnasrulloh.simplegithubuser.data.local.preferences.DarkModePreferences
import com.davidnasrulloh.simplegithubuser.ui.setting.SettingViewModel


class SplashFactory(private val pref: DarkModePreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}