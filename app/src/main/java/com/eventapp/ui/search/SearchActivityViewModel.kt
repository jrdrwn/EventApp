package com.eventapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.remote.response.ListEventsItem
import com.eventapp.repository.SearchEventsRepository
import com.eventapp.utils.Result

class SearchActivityViewModel(private val listEventsRepository: SearchEventsRepository) :
    ViewModel() {
    lateinit var listEvents: LiveData<Result<List<ListEventsItem>>>

    fun searchEvents(keyword: String): LiveData<Result<List<ListEventsItem>>> {
        listEvents = listEventsRepository.searchEvents(keyword)
        return listEvents
    }
}