package com.example.myscrapnel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyScrapnelViewModelFactory(private val repository: SaveScrapnelRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyScrapnelViewModel(repository) as T
    }
}
