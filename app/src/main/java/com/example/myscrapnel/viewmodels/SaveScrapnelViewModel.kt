package com.example.myscrapnel.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myscrapnel.room_db.ScrapnelEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyScrapnelViewModel(private val repository: SaveScrapnelRepository) : ViewModel() {

    private val _saveResult = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val saveResult: StateFlow<Result<Boolean>> = _saveResult

    fun saveScrapnel(scrapnel: ScrapnelEntity) {
        viewModelScope.launch {
            try {
                val existsNearby = repository.isScrapnelWithinFiveMinutes(scrapnel.timeStamp)
                if (!existsNearby) {
                    repository.insertScrapnel(scrapnel)
                    _saveResult.value = Result.success(true)
                } else {
                    _saveResult.value = Result.failure(Exception("Scrapnel already exists within 5 minutes."))
                }
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }
}

