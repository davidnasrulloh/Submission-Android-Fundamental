package com.davidnasrulloh.simplegithubuser.ui.detail.following

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidnasrulloh.simplegithubuser.data.network.api.ApiConfig
import com.davidnasrulloh.simplegithubuser.data.network.response.SimpleUser
import com.davidnasrulloh.simplegithubuser.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel() : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _following = MutableLiveData<ArrayList<SimpleUser>?>(null)
    val following: LiveData<ArrayList<SimpleUser>?> = _following

    fun getUserFollowing(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowing(token = "Bearer ${Utils.TOKEN}", username)
            .apply {
                enqueue(object : Callback<ArrayList<SimpleUser>> {
                    override fun onResponse(
                        call: Call<ArrayList<SimpleUser>>,
                        response: Response<ArrayList<SimpleUser>>
                    ) {
                        if (response.isSuccessful) _following.value = response.body()
                        else Log.e(TAG, response.message())
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<ArrayList<SimpleUser>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _following.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
}