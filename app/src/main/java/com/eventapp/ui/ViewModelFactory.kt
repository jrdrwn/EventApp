package com.eventapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventapp.di.Injection
import com.eventapp.repository.EventRepository
import com.eventapp.repository.FinishedEventsRepository
import com.eventapp.repository.SearchEventsRepository
import com.eventapp.repository.UpcomingEventsRepository
import com.eventapp.ui.detail.DetailActivityViewModel
import com.eventapp.ui.finished.FinishedViewModel
import com.eventapp.ui.search.SearchActivityViewModel
import com.eventapp.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val upcomingEventsRepository: UpcomingEventsRepository,
    private val finishedEventsRepository: FinishedEventsRepository,
    private val searchEventsRepository: SearchEventsRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailActivityViewModel::class.java)) {
            return DetailActivityViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(upcomingEventsRepository) as T
        } else if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(finishedEventsRepository) as T
        } else if (modelClass.isAssignableFrom(SearchActivityViewModel::class.java)) {
            return SearchActivityViewModel(searchEventsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideUpcomingEventsRepository(context),
                    Injection.provideFinishedEventsRepository(context),
                    Injection.provideSearchEventsRepository(context)
                )
            }.also { instance = it }
    }
}