package com.example.myscrapnel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myscrapnel.models.scrapnel_ui_model.ScrapnelUiModel
import com.example.myscrapnel.room_db.ScrapnelEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class ViewScrapnelViewModel(private val repository: ViewScrapnelRepository) : ViewModel() {

    private val _scrapnelItems = MutableStateFlow<List<ScrapnelUiModel>>(emptyList())
    val scrapnelItems: StateFlow<List<ScrapnelUiModel>> = _scrapnelItems

    private val _chipTitles = MutableStateFlow<List<String>>(emptyList())
    val chipTitles: StateFlow<List<String>> = _chipTitles
    private val _selectedItemsToDelete = MutableStateFlow<List<ScrapnelUiModel>>(emptyList())
    val selectedItemsToDelete: StateFlow<List<ScrapnelUiModel>> = _selectedItemsToDelete
    private val _scrapnel = MutableStateFlow<ScrapnelEntity?>(null)
    val scrapnel: StateFlow<ScrapnelEntity?> = _scrapnel




    fun loadChipTitles() {
        viewModelScope.launch {
            _chipTitles.value = repository.getScrapnelTitleChips()
        }
    }
    fun loadScrapnels() {
        viewModelScope.launch {
            _scrapnelItems.value = repository.getAllScrapnelUiModels()
        }
    }

    fun loadScrapnel(timestamp: Long?) {
        viewModelScope.launch {
            val scrapnel = repository.getScrapnelEntityByTimestamp(timestamp)
            if (scrapnel != null) {
                _scrapnel.value = scrapnel
            }
        }
    }

    fun loadFilteredScrapnels(filterKey: String) {
        viewModelScope.launch {
            _scrapnelItems.value = repository.getFilteredScrapnels(filterKey)
            _chipTitles.value = repository.getFilteredChips(filterKey)
        }

    }

    fun loadScrapnelsByTitle(title: String) {
        viewModelScope.launch {
            _scrapnelItems.value = repository.getScrapnelsByTitle(title)
        }
    }


    fun selectCheckedItems(item: ScrapnelUiModel, isChecked: Boolean) {
        _selectedItemsToDelete.value = if (isChecked) {
            _selectedItemsToDelete.value + item
        } else {
            _selectedItemsToDelete.value.filterNot { it.timeStamp == item.timeStamp }
        }
    }


    fun clearSelectedItems() {
        _selectedItemsToDelete.value = emptyList()
    }


    fun deleteSelectedItems() {
        viewModelScope.launch {
            val itemsToDelete = _selectedItemsToDelete.value

            val entitiesToDelete = itemsToDelete.mapNotNull { item ->
                repository.getScrapnelEntityByTimestamp(item.timeStamp)
            }

            repository.deleteScrapnel(entitiesToDelete)
            clearSelectedItems()
            loadScrapnels()
            loadChipTitles()
        }
    }



}
