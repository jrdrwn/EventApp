package com.eventapp.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.remote.response.ListEventsItem
import com.eventapp.repository.UpcomingEventsRepository
import com.eventapp.utils.Result

class UpcomingViewModel(private val upcomingEventsRepository: UpcomingEventsRepository) :
    ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun getUpcomingEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        listEvents = upcomingEventsRepository.getUpcomingEvent(limit)
        return listEvents
    }
}