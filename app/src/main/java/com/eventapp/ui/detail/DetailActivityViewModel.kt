package com.eventapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.response.DetailEventResponse
import com.eventapp.data.response.Event
import com.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivityViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetails(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.event
                    _errorMessage.value = ""
                } else {
                    Log.e("DetailActivityViewModel", response.message())
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailActivityViewModel", "onFailure: ${t.message.toString()}")
                _errorMessage.value = t.message.toString()
            }
        })
    }
}