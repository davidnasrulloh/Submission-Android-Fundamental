package com.davidnasrulloh.simplegithubuser.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.davidnasrulloh.simplegithubuser.data.local.preferences.DarkModePreferences
import com.davidnasrulloh.simplegithubuser.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")
@SuppressLint("CustomSplashScreen")
class SplashActivity() : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = DarkModePreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, SplashFactory(preferences))[SplashViewModel::class.java]

        viewModel.isDarkMode().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Intent(this, MainActivity::class.java).also {
                    runBlocking {
                        delay(300)
                        startActivity(it)
                        finish()
                    }
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Intent(this, MainActivity::class.java).also {
                    runBlocking {
                        delay(300)
                        startActivity(it)
                        finish()
                    }
                }
            }
        }


    }
}