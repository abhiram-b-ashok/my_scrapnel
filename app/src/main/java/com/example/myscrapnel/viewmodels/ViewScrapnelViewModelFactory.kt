package com.example.myscrapnel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.jvm.java

class ViewScrapnelViewModelFactory(
    private val repository: ViewScrapnelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewScrapnelViewModel::class.java)) {
            return ViewScrapnelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
