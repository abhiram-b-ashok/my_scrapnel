package com.example.myscrapnel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myscrapnel.models.scrapnel_ui_model.ScrapnelUiModel
import com.example.myscrapnel.room_db.ScrapnelEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ViewScrapnelViewModel(private val repository: ViewScrapnelRepository) : ViewModel() {

    private val _scrapnelItems = MutableStateFlow<List<ScrapnelUiModel>>(emptyList())
    val scrapnelItems: StateFlow<List<ScrapnelUiModel>> = _scrapnelItems



    fun loadScrapnels() {
        viewModelScope.launch {
            _scrapnelItems.value = repository.getAllScrapnelUiModels()
        }
    }

    fun loadFilteredScrapnels(filterKey: String) {
        viewModelScope.launch {
            _scrapnelItems.value = repository.getFilteredScrapnels(filterKey)
        }

    }

    fun deleteTheseItemsFromDb(scrapnels: List<ScrapnelEntity>) {
        viewModelScope.launch {
            repository.deleteScrapnel(scrapnels)
        }
    }


}
