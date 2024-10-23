package com.eventapp.di

import android.content.Context
import com.eventapp.data.remote.retrofit.ApiConfig
import com.eventapp.repository.EventRepository
import com.eventapp.repository.FinishedEventsRepository
import com.eventapp.repository.SearchEventsRepository
import com.eventapp.repository.UpcomingEventsRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }

    fun provideUpcomingEventsRepository(context: Context): UpcomingEventsRepository {
        val apiService = ApiConfig.getApiService()
        return UpcomingEventsRepository.getInstance(apiService)
    }

    fun provideFinishedEventsRepository(context: Context): FinishedEventsRepository {
        val apiService = ApiConfig.getApiService()
        return FinishedEventsRepository.getInstance(apiService)
    }

    fun provideSearchEventsRepository(context: Context): SearchEventsRepository {
        val apiService = ApiConfig.getApiService()
        return SearchEventsRepository.getInstance(apiService)
    }
}