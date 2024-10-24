package com.eventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventapp.data.local.entity.FavoriteEvent
import com.eventapp.data.remote.response.Event
import com.eventapp.repository.EventRepository
import com.eventapp.repository.FavoriteEventRepository
import com.eventapp.utils.Result
import kotlinx.coroutines.launch

class DetailActivityViewModel(
    private val eventRepository: EventRepository,
    private val favoriteEventRepository: FavoriteEventRepository
) : ViewModel() {
    lateinit var event: LiveData<Result<Event>>
    lateinit var favoriteEvent: LiveData<FavoriteEvent>

    fun getDetailEvent(id: Int): LiveData<Result<Event>> {
        event = eventRepository.getDetailEvent(id)
        return event
    }

    fun insert(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.insert(favoriteEvent)
        }
    }

    fun getFavoriteEventById(id: Int):
            LiveData<FavoriteEvent> {
        favoriteEvent = favoriteEventRepository.getFavoriteEventById(id)
        return favoriteEvent
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.delete(favoriteEvent)
        }
    }
}