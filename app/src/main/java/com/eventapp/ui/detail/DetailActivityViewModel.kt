package com.eventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.remote.response.Event
import com.eventapp.repository.EventRepository
import com.eventapp.utils.Result

class DetailActivityViewModel(private val eventRepository: EventRepository) : ViewModel() {
    lateinit var event: LiveData<Result<Event>>

    fun getDetailEvent(id: Int): LiveData<Result<Event>> {
        event = eventRepository.getDetailEvent(id)
        return event
    }
}