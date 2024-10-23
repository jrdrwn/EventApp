package com.eventapp.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.remote.response.ListEventsItem
import com.eventapp.repository.FinishedEventsRepository
import com.eventapp.utils.Result

class FinishedViewModel(private val listEventsRepository: FinishedEventsRepository) : ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun getFinishedEvent(limit: Int): LiveData<Result<List<ListEventsItem>>> {
        listEvents = listEventsRepository.getFinishedEvent(limit)
        return listEvents
    }
}