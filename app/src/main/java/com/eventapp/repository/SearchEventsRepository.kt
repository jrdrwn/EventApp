package com.eventapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.eventapp.data.remote.response.EventResponse
import com.eventapp.data.remote.response.ListEventsItem
import com.eventapp.data.remote.retrofit.ApiService
import com.eventapp.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchEventsRepository private constructor(private val apiService: ApiService) {

    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()


    fun searchEvents(keyword: String): LiveData<Result<List<ListEventsItem>>> {
        result.value = Result.Loading
        val client = apiService.searchEvents(keyword)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!.listEvents)
                    if (response.body()?.listEvents?.isEmpty() == true) {
                        result.value =
                            Result.Error("Tidak ada hasil pencarian untuk \"${keyword}\"")
                    }
                } else {
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    companion object {
        @Volatile
        private var instance: SearchEventsRepository? = null

        fun getInstance(apiService: ApiService): SearchEventsRepository {
            return instance ?: synchronized(this) {
                instance ?: SearchEventsRepository(apiService).also { instance = it }
            }
        }
    }
}