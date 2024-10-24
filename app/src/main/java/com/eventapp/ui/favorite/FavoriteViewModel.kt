package com.eventapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eventapp.data.local.entity.FavoriteEvent
import com.eventapp.repository.FavoriteEventRepository

class FavoriteViewModel(private val favoriteEventRepository: FavoriteEventRepository) :
    ViewModel() {
    lateinit var favoriteEvent: LiveData<List<FavoriteEvent>>

    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        favoriteEvent = favoriteEventRepository.getFavoriteEvents()
        return favoriteEvent
    }

}